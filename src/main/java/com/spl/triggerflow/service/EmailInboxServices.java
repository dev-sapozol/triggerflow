package com.spl.triggerflow.service;

import org.springframework.stereotype.Service;
import com.spl.triggerflow.dto.email_inbox.CreateEmailInput;
import com.spl.triggerflow.dto.email_inbox.EmailInboxFilter;
import com.spl.triggerflow.dto.email_inbox.ForwardEmailInput;
import com.spl.triggerflow.dto.email_inbox.GetEmailList;
import com.spl.triggerflow.dto.email_inbox.ReplyEmailInput;
import com.spl.triggerflow.entity.EmailInboxEntity;
import com.spl.triggerflow.repository.EmailInboxRepository;
import com.spl.triggerflow.security.CustomUserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class EmailInboxServices {

  private final EmailInboxRepository emailInboxRepository;

  private static final Logger log = LoggerFactory.getLogger(EmailInboxServices.class);
  private final JavaMailSender mailSender;
  private final SqsAsyncClient sqsAsyncClient;

  @Value("${sqs.receive.email.name}")
  private String queueUrl;

  public EmailInboxServices(JavaMailSender mailSender, SqsAsyncClient sqsAsyncClient,
      EmailInboxRepository emailInboxRepository) {
    this.mailSender = mailSender;
    this.sqsAsyncClient = sqsAsyncClient;
    this.emailInboxRepository = emailInboxRepository;
  }

  @Scheduled(cron = "0 */30 * * * *")
  public void pollQueueReceiveEmail() {
    log.info("Iniciando sondeo de la cola SQS...");
    ReceiveMessageRequest request = ReceiveMessageRequest.builder()
        .queueUrl(queueUrl)
        .waitTimeSeconds(20)
        .maxNumberOfMessages(10)
        .build();

    sqsAsyncClient.receiveMessage(request).thenAccept(response -> {
      List<Message> messages = response.messages();
      if (!messages.isEmpty()) { // Solo loguea si hay mensajes
        log.info("Recibidos {} mensajes de SQS.", messages.size());
      }
      for (Message message : messages) {
        log.info("Procesando mensaje: {}", message.messageId());
        // Procesar el cuerpo del mensaje (message.body())

        sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(message.receiptHandle())
            .build()).whenComplete((deleteResponse, deleteEx) -> { // Opcional: manejar resultado del delete
              if (deleteEx != null) {
                log.error("Error al eliminar mensaje {} de SQS: ", message.messageId(), deleteEx);
              } else {
                log.info("Mensaje {} eliminado de SQS.", message.messageId());
              }
            });
      }
    }).exceptionally(ex -> {
      log.error("Error al recibir mensajes de SQS: ", ex);
      return null;
    });
  }

  public List<GetEmailList> getEmailList(Authentication auth, EmailInboxFilter f) {
    Long userId = ((CustomUserDetails) auth.getPrincipal()).getUser().getId();
    EmailInboxFilter filter = (f == null) ? new EmailInboxFilter() : f;

    return emailInboxRepository.findFiltered(
        userId,
        filter.getIsRead(),
        filter.getImportance(),
        filter.getHasAttachment(),
        filter.getFolder(),
        filter.getTo(),
        filter.getCc(),
        filter.getDeletedAt() != null ? Boolean.TRUE : null)
        .stream()
        .map(this::toDto)
        .toList();
  }

  private GetEmailList toDto(EmailInboxEntity e) {
    GetEmailList dto = new GetEmailList();
    dto.setId(e.getId());
    dto.setFrom(e.getFrom());
    dto.setTo(e.getTo());
    dto.setCc(e.getCc());
    dto.setSubject(e.getSubject());
    dto.setPreview(e.getPreview());
    dto.setIsRead(e.getIsRead());
    dto.setHasAttachment(e.getHasAttachment());
    dto.setImportance(e.getImportance());
    dto.setThreadId(e.getThreadId());
    dto.setFolder(e.getFolder());
    dto.setDeletedAt(e.getDeletedAt());
    dto.setCreatedAt(e.getCreatedAt());
    return dto;
  }

  public List<EmailInboxEntity> getBasicEmailList(Authentication auth) {
    Long userId = ((CustomUserDetails) auth.getPrincipal()).getUser().getId();
    int folderWanted = 1;

    return emailInboxRepository.findAllByUserIdAndFolder(userId, folderWanted)
        .stream()
        .map(e -> new EmailInboxEntity(
            e.getId(),
            e.getFrom(),
            e.getTo(),
            e.getCc(),
            e.getSubject(),
            e.getPreview(),
            e.getIsRead(),
            e.getHasAttachment(),
            e.getImportance(),
            e.getThreadId(),
            e.getFolder(),
            e.getDeletedAt(),
            e.getCreatedAt()))
        .collect(Collectors.toList());
  }

  public void sendEmail(CreateEmailInput emailInput) throws MessagingException {
    log.info("INICIANDO ENVIO DE CORREO");
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
    // String fromText = emailInput.getFrom();

    try {
      InternetAddress[] addresses = InternetAddress.parse(emailInput.getFrom(), true);
      if (addresses.length != 1) {
        throw new IllegalArgumentException("Formato de correo no válido: debe ser una única dirección válida");
      }
      helper.setFrom(addresses[0]);
    } catch (Exception e) {
      throw new IllegalArgumentException("Formato de correo no válido", e);
    }

    String[] toAddresses = parseAddresses(emailInput.getTo());
    if (toAddresses.length > 0)
      helper.setTo(toAddresses);
    else
      throw new IllegalArgumentException("Recipiente 'to' no es válido");

    if (emailInput.getCc() != null && !emailInput.getCc().trim().isEmpty()) {
      String[] ccAddresses = parseAddresses(emailInput.getCc());
      if (ccAddresses.length > 0)
        helper.setCc(ccAddresses);
    }

    if (emailInput.getBcc() != null && !emailInput.getBcc().trim().isEmpty()) {
      String[] bccAddresses = parseAddresses(emailInput.getBcc());
      if (bccAddresses.length > 0)
        helper.setBcc(bccAddresses);
    }

    helper.setSubject(emailInput.getSubject());
    boolean isHtml = emailInput.getHtmlBody() != null && !emailInput.getHtmlBody().trim().isEmpty();
    helper.setText(isHtml ? emailInput.getHtmlBody() : emailInput.getTextBody(), isHtml);
    mailSender.send(mimeMessage);
    log.info("Correo para `{}` enviado con éxito", emailInput.getTo());
  }

  public void replyEmail(ReplyEmailInput replyEmail) throws MessagingException {

  }

  public void forwardEmail(ForwardEmailInput forwardEmail) throws MessagingException {

  }

  private String[] parseAddresses(String addressesString) {
    if (addressesString == null || addressesString.trim().isEmpty()) {
      return new String[0];
    }
    return Arrays.stream(addressesString.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toArray(String[]::new);
  }

  public EmailInboxEntity getEmailById(Long id) {
    return emailInboxRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Emmail not found with id: " + id));
  }
}

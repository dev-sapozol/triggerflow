package com.spl.triggerflow.service;

import org.springframework.stereotype.Service;
import com.spl.triggerflow.dto.email_inbox.CreateEmailInput;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class EmailInboxServices {

  private static final Logger log = LoggerFactory.getLogger(EmailInboxServices.class);
  private final JavaMailSender mailSender;
  private final SqsAsyncClient sqsAsyncClient;

  @Value("${sqs.receive.email.name}")
  private String queueUrl;

  public EmailInboxServices(JavaMailSender mailSender, SqsAsyncClient sqsAsyncClient) {
    this.mailSender = mailSender;
    this.sqsAsyncClient = sqsAsyncClient;
  }

  @Scheduled(cron = "0 */5 * * * *")
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

  public void sendEmail(CreateEmailInput emailInput) throws MessagingException {
    log.info("INICIANDO ENVIO DE CORREO");
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

    helper.setFrom(emailInput.getFrom());
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

  private String[] parseAddresses(String addressesString) {
    if (addressesString == null || addressesString.trim().isEmpty()) {
      return new String[0];
    }
    return Arrays.stream(addressesString.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toArray(String[]::new);
  }
}

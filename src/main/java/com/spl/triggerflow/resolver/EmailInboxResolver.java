package com.spl.triggerflow.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.mail.MailException;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Controller;

import com.spl.triggerflow.dto.email_inbox.CreateEmailInput;
import com.spl.triggerflow.dto.email_inbox.EmailInboxFilter;
import com.spl.triggerflow.dto.email_inbox.GetEmailList;
import com.spl.triggerflow.entity.EmailInboxEntity;
import com.spl.triggerflow.repository.EmailInboxRepository;
import com.spl.triggerflow.service.EmailInboxServices;

import jakarta.validation.Valid;

@Controller
public class EmailInboxResolver {

  private static final Logger log = LoggerFactory.getLogger(EmailInboxServices.class);
  private final EmailInboxRepository emailInboxRepository;
  private final EmailInboxServices emailInboxServices;

  public EmailInboxResolver(EmailInboxRepository emailInboxRepository, EmailInboxServices emailInboxServices) {
    this.emailInboxRepository = emailInboxRepository;
    this.emailInboxServices = emailInboxServices;
  }

  @QueryMapping
  public List<GetEmailList> getEmailList(@Argument EmailInboxFilter input,
      Authentication auth) {
    return emailInboxServices.getEmailList(auth, input);
  }

  @QueryMapping
  public List<EmailInboxEntity> getBasicEmailList(Authentication auth) {
    return emailInboxServices.getBasicEmailList(auth);
  }

  @QueryMapping
  public EmailInboxEntity getEmailByID(@Argument Long id) {
    return emailInboxServices.getEmailById(id);
  }

  @MutationMapping
  public EmailInboxEntity createEmail(@Argument @Valid CreateEmailInput input) {

    try {
      emailInboxServices.sendEmail(input);
      log.info("Llamada a emailInboxServices.sendEmail completada sin excepciones visibles aquí.");
    } catch (MessagingException me) {
      log.error("MessagingException al enviar correo: {}", me.getMessage(), me);
    } catch (MailException mae) { // Captura excepciones más generales de Spring Mail
      log.error("MailException al enviar correo: {}", mae.getMessage(), mae);
    } catch (Exception e) { // Captura cualquier otra cosa
      log.error("Excepción inesperada durante el envío del correo: {}", e.getMessage(), e);
    }

    EmailInboxEntity email = new EmailInboxEntity();
    email.setUserId(input.getUserId());
    email.setFrom(input.getFrom());
    email.setTo(input.getTo());
    email.setCc(input.getCc());
    email.setSubject(input.getSubject());
    email.setPreview(input.getPreview());
    email.setInboxType(input.getInboxType());
    email.setIsRead(input.getIsRead());
    email.setHasAttachment(input.getHasAttachment());
    email.setImportance(input.getImportance());
    email.setTextBody(input.getTextBody());
    email.setHtmlBody(input.getHtmlBody());
    email.setFolder(input.getFolder());
    return emailInboxRepository.save(email);
  }
}

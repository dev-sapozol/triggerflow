package com.spl.triggerflow.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@Service
public class ScheduledTaskListenerServices {

  private static final Logger log = LoggerFactory.getLogger(ScheduledTaskListenerServices.class);
  private SqsAsyncClient sqsAsyncClient;

  @Value("${sqs.automation.name}")
  private String queueUrl;

  public ScheduledTaskListenerServices(SqsAsyncClient sqsAsyncClient) {
    this.sqsAsyncClient = sqsAsyncClient;
  }

  @Scheduled(cron = "0 */30 * * * *")
  public void pollQueueReceiveScheduledTask() {
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
}
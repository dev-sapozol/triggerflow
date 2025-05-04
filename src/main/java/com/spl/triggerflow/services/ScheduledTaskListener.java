package com.spl.triggerflow.services;

import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskListener {

  private static final Logger log = LoggerFactory.getLogger(ScheduledTaskListener.class);

  @Value("${sqs.automation.name}")
  private String sqsAutomationName;

  // Log para confirmar que Spring creó este bean
  @PostConstruct
  public void init() {
    log.info("<<<<< ScheduledTaskListener Bean Initialized >>>>>");
  }

  @SqsListener("${sqs.automation.name}")
  public void receiveMessage(String payload) {
    log.info("Received SQS message from queue {} with payload: {}", sqsAutomationName, payload);
  }
}
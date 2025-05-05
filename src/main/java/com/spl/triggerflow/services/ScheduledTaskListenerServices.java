package com.spl.triggerflow.services;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskListenerServices {

  private static final Logger log = LoggerFactory.getLogger(ScheduledTaskListenerServices.class);

  @SqsListener("${sqs.automation.name}")
  public void receiveMessage(String payload) {
    log.info("Received SQS message, payload: {}", payload);
  }
}
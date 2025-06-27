package com.spl.triggerflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfigServices {

  private static final Logger log = LoggerFactory.getLogger(AwsConfigServices.class);
  private static final String awsAccountId = System.getenv("AWS_ACCOUNT_ID");
  private static final String environment = System.getenv("ENVIRONMENT") != null
      ? System.getenv("ENVIRONMENT")
      : "dev";

  public static String buildSqsArn() {
    log.info(awsAccountId, environment);
    return "arn:aws:sqs:us-east-1:" + awsAccountId + ":sqs-automation-email-" + environment;
  }

  public static String buildSqsHttpUrl() {
    return "https://sqs.us-east-1.amazonaws.com/" + awsAccountId + "/sqs-automation-email-" + environment;
  }

  public static String buildRoleArn() {
    return "arn:aws:iam::" + awsAccountId + ":role/RoleAutomationEmail";
  }
}
package com.spl.triggerflow.services;
import io.github.cdimascio.dotenv.Dotenv;

public class AwsConfigServices {

  private static final Dotenv dotenv = Dotenv.configure()
    .filename(".env")
    .load();

  private static final String awsAccountId = dotenv.get("AWS_ACCOUNT_ID");
  private static final String environment = dotenv.get("ENVIRONMENT") != null
    ? dotenv.get("ENVIRONMENT")
    : "dev";

    public static String buildSqsArn() {
      return "arn:aws:sqs:us-east-1:" + awsAccountId + ":sqs-automation-email-" + environment;
    }

    public static String buildSqsHttpUrl() {
      return "https://sqs.us-east-1.amazonaws.com/" + awsAccountId + "/sqs-automation-email-" + environment;
    }

    public static String buildRoleArn() {
      return "arn:aws:iam::" + awsAccountId + ":role/RoleAutomationEmail";
    }
}
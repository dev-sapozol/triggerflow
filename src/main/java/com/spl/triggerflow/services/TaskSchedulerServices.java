package com.spl.triggerflow.services;

import java.util.UUID;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.*;


public class TaskSchedulerServices {

  public static void main (String[] args) {
    
    String scheduleName = "automation-" + UUID.randomUUID().toString();
    String roleArn = AwsConfigServices.buildRoleArn();
    String sqsArn = AwsConfigServices.buildSqsArn();

    final SchedulerClient schedulerClient = SchedulerClient.builder()
      .region(Region.US_EAST_1)
      .build();

    Target sqsTarget = Target.builder()
      .roleArn(roleArn)
      .arn(sqsArn)
      .input(
        "Message: Hello from Triggerflow, scheduleTime: '<aws.scheduler.scheduled-time>'")
      .build();

    CreateScheduleRequest createScheduleRequest = CreateScheduleRequest.builder()
      .name(scheduleName)
      .scheduleExpression("cron(0/5 * * * ? *)")
      .target(sqsTarget)
      .flexibleTimeWindow(FlexibleTimeWindow.builder()
        .mode(FlexibleTimeWindowMode.OFF)
        .build())
      .build();

    schedulerClient.createSchedule(createScheduleRequest);
    System.out.println("Schedule created");
  }
}

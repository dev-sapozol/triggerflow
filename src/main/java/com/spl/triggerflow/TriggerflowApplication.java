package com.spl.triggerflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TriggerflowApplication {
	public static void main(String[] args) {
		SpringApplication.run(TriggerflowApplication.class, args);
	}

}

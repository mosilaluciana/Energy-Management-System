package com.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringSimulatorApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringSimulatorApplication.class, args);
	}

}

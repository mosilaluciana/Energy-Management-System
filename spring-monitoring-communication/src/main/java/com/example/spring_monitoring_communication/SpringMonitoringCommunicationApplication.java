package com.example.spring_monitoring_communication;

import com.example.spring_monitoring_communication.rabbitMQ.MessageReceiver;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableRabbit
public class SpringMonitoringCommunicationApplication {

	public static void main(String[] args) {
	 SpringApplication.run(SpringMonitoringCommunicationApplication.class, args);

	}
}


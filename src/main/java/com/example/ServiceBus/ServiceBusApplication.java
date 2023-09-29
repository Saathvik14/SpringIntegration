package com.example.ServiceBus;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info= @Info(title = "Service Applicaion", version = "3.0",description = "ServiceBus Application"))
public class ServiceBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBusApplication.class, args);
	}

}

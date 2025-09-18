package com.backend.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

		System.out.println("-----------------------------------------");
		System.out.println("Backend application started successfully.");
		System.out.println("-----------------------------------------");
	}

}

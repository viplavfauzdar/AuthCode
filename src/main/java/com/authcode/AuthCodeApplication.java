package com.authcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;

@SpringBootApplication
public class AuthCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthCodeApplication.class, args);
	}
		
}

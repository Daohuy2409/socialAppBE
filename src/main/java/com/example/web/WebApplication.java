package com.example.web;

import com.example.web.service.EmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@SpringBootApplication
public class WebApplication {

	private EmailService emailService;
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}


}

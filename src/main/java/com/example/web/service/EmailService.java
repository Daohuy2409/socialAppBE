package com.example.web.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceLoader resourceLoader;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.resourceLoader = resourceLoader;
    }

    public void sendHTMLMail(String name, String to, String body)
            throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, to);

        String htmlTemplate = readFile("classpath:templates/emailTemplate.html");
        String htmlContent = htmlTemplate.replace("${name}", name);
        htmlContent = htmlContent.replace("${message}", body);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public String readFile(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource(filePath);
        return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
    }
}

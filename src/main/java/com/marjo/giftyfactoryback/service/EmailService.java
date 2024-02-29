package com.marjo.giftyfactoryback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(long userId, String email, String token) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Test email from Spring");

        String htmlContent = "<h1>Confirm your email to finish your subcription to Gifty</h1>" +
                "<p>Click on next link to activate your account :"+
                "<a href='http://localhost:8081/api/auth/activate/" + userId + "/" + token + "'/>Confirm link</a></p>";
                // TODO : Send a prettier email
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
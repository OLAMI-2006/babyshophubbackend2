package com.babyshophub.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Modern Constructor Injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("BabyShopHub - Email Verification Code");
        message.setText("Your verification code is: " + verificationCode + 
                        "\n\nPlease use this code to activate your account. It expires in 15 minutes.");
        mailSender.send(message);
    }
}
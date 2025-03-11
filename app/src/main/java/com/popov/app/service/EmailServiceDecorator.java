package com.popov.app.service;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceDecorator extends EmailService {

    private final EmailService emailService;

    public EmailServiceDecorator(EmailService emailService) {
        super(emailService.mailSender);
        this.emailService = emailService;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        emailService.sendEmail(to, subject, text);
        System.out.println("Email sent: " + subject);
    }

    public void notifyAspectExecution(String method, String details) {
        String message = "Aspect triggered in method: " + method + "\nDetails: " + details;
        sendEmail("clemsonnn22@gmail.com", "Aspect Execution Notification", message);
    }
}

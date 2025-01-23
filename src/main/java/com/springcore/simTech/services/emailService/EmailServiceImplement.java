package com.springcore.simTech.services.emailService;

import com.springcore.simTech.dto.requests.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImplement implements EmailService {

    final JavaMailSender javaMailSender;

    @Value("$spring.mail.username")
    private String senderEmail;

    @Override
    public void sendEmail(EmailRequest emailRequest) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(emailRequest.getRecipient());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getMessageBody());
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}

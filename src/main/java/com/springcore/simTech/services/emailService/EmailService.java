package com.springcore.simTech.services.emailService;

import com.springcore.simTech.dto.requests.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest emailRequest);
}

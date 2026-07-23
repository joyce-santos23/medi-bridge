package br.com.medibridge.medi_bridge.notification.core.application.service;

import br.com.medibridge.medi_bridge.notification.core.application.dto.EmailRequestDTO;
import br.com.medibridge.medi_bridge.notification.core.application.port.EmailSenderGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailSenderGateway emailSenderGateway;

    public void sendEmail(String to, String subject, String body) {
        log.info("Preparing to send email to: {} with subject: {}", to, subject);
        EmailRequestDTO request = new EmailRequestDTO(to, subject, body);
        emailSenderGateway.send(request);
        log.info("Email sent request delegated successfully for to: {}", to);
    }
}

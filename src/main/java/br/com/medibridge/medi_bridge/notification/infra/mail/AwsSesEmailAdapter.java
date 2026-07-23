package br.com.medibridge.medi_bridge.notification.infra.mail;

import br.com.medibridge.medi_bridge.notification.core.application.dto.EmailRequestDTO;
import br.com.medibridge.medi_bridge.notification.core.application.port.EmailSenderGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsSesEmailAdapter implements EmailSenderGateway {

    private final SesClient sesClient;

    @Value("${aws.ses.fromEmail}")
    private String fromEmail;

    @Override
    public void send(EmailRequestDTO request) {
        log.info("Sending email via AWS SES from {} to {}", fromEmail, request.to());
        try {
            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(Destination.builder()
                            .toAddresses(request.to())
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data(request.subject())
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .text(Content.builder()
                                            .data(request.body())
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = sesClient.sendEmail(sendEmailRequest);
            log.info("Email successfully sent via AWS SES. Message ID: {}", response.messageId());
        } catch (SesException e) {
            log.error("Failed to send email via AWS SES to: {}", request.to(), e);
            throw new RuntimeException("Error sending email via AWS SES", e);
        }
    }
}

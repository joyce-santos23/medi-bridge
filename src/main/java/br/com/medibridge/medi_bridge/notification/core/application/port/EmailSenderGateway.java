package br.com.medibridge.medi_bridge.notification.core.application.port;

import br.com.medibridge.medi_bridge.notification.core.application.dto.EmailRequestDTO;

public interface EmailSenderGateway {
    void send(EmailRequestDTO request);
}

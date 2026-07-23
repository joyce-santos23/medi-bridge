package br.com.medibridge.medi_bridge.notification.infra.integration;

import br.com.medibridge.medi_bridge.notification.core.application.dto.integration.NotificationTransferDetails;
import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationTransferGateway;
import br.com.medibridge.medi_bridge.transfer.core.application.port.TransferGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransferIntegrationAdapter implements NotificationTransferGateway {

    private final TransferGateway transferGateway;

    @Override
    public Optional<NotificationTransferDetails> findTransferById(UUID id) {
        return transferGateway.findById(id)
                .map(transfer -> new NotificationTransferDetails(
                        transfer.getId(),
                        transfer.getOfferId(),
                        transfer.getSourceHospitalId(),
                        transfer.getDestinationHospitalId(),
                        transfer.getReason(),
                        transfer.getConfirmationCode()
                ));
    }
}

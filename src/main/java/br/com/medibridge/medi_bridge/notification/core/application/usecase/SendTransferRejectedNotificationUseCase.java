package br.com.medibridge.medi_bridge.notification.core.application.usecase;

import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationCatalogGateway;
import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationOfferGateway;
import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationTransferGateway;
import br.com.medibridge.medi_bridge.notification.core.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendTransferRejectedNotificationUseCase {

    private final NotificationCatalogGateway catalogGateway;
    private final NotificationOfferGateway offerGateway;
    private final NotificationTransferGateway transferGateway;
    private final NotificationService notificationService;

    public void execute(UUID transferId) {
        log.info("Executing SendTransferRejectedNotificationUseCase for transfer ID: {}", transferId);

        var transfer = transferGateway.findTransferById(transferId).orElse(null);
        if (transfer == null) {
            log.warn("Transfer not found: {}. Skipping notification.", transferId);
            return;
        }

        var offer = offerGateway.findOfferById(transfer.offerId()).orElse(null);
        var donorHospital = catalogGateway.findHospitalById(transfer.sourceHospitalId()).orElse(null);
        var requesterHospital = catalogGateway.findHospitalById(transfer.destinationHospitalId()).orElse(null);

        if (donorHospital == null || requesterHospital == null || offer == null) {
            log.warn("Required details for transfer notification not found. Skipping.");
            return;
        }

        String subject = "Solicitação de Transferência Rejeitada - MediBridge";
        String body = String.format(
                "Olá, %s,\n\nInfelizmente, sua solicitação de transferência foi rejeitada pelo hospital doador.\n\n" +
                "Detalhes da Solicitação:\n" +
                "- Hospital Doador: %s\n" +
                "- Insumo: %s\n" +
                "- Quantidade: %d %s\n\n" +
                "Justificativa da Rejeição:\n" +
                "\"%s\"\n\n" +
                "Atenciosamente,\nEquipe MediBridge",
                requesterHospital.name(),
                donorHospital.name(),
                offer.medicineName(),
                offer.quantity(),
                offer.unit(),
                transfer.reason() != null ? transfer.reason() : "Nenhuma justificativa informada."
        );

        notificationService.sendEmail(requesterHospital.email(), subject, body);
    }
}

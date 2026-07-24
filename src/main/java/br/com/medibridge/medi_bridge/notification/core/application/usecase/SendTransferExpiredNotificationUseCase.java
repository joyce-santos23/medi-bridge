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
public class SendTransferExpiredNotificationUseCase {

    private final NotificationCatalogGateway catalogGateway;
    private final NotificationOfferGateway offerGateway;
    private final NotificationTransferGateway transferGateway;
    private final NotificationService notificationService;

    public void execute(UUID transferId) {
        log.info("Executing SendTransferExpiredNotificationUseCase for transfer ID: {}", transferId);

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

        String subject = "Solicitação de Transferência Expirada - MediBridge";
        String body = String.format(
                "Olá,\n\nInformamos que a solicitação de transferência expirou porque o prazo de retirada terminou.\n\n" +
                "Detalhes da Transferência:\n" +
                "- Hospital Doador: %s\n" +
                "- Hospital Solicitante: %s\n" +
                "- Insumo: %s\n" +
                "- Quantidade: %d %s\n\n" +
                "Com isso, a oferta do insumo voltou a ficar disponível na plataforma para outras solicitações.\n\n" +
                "Atenciosamente,\nEquipe MediBridge",
                donorHospital.name(),
                requesterHospital.name(),
                offer.medicineName(),
                offer.quantity(),
                offer.unit()
        );

        // Envia para ambos os hospitais
        try {
            notificationService.sendEmail(donorHospital.email(), subject, body);
        } catch (Exception e) {
            log.error("Failed to send TransferExpired notification to donor hospital: {}", donorHospital.id(), e);
        }

        try {
            notificationService.sendEmail(requesterHospital.email(), subject, body);
        } catch (Exception e) {
            log.error("Failed to send TransferExpired notification to requester hospital: {}", requesterHospital.id(), e);
        }
    }
}

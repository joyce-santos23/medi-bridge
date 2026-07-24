package br.com.medibridge.medi_bridge.notification.core.application.usecase;

import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationCatalogGateway;
import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationOfferGateway;
import br.com.medibridge.medi_bridge.notification.core.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendOfferCreatedNotificationUseCase {

    private final NotificationCatalogGateway catalogGateway;
    private final NotificationOfferGateway offerGateway;
    private final NotificationService notificationService;

    public void execute(UUID offerId, UUID publisherHospitalId) {
        log.info("Executing SendOfferCreatedNotificationUseCase for offer ID: {}", offerId);

        var offer = offerGateway.findOfferById(offerId).orElse(null);
        if (offer == null) {
            log.warn("Offer not found: {}. Skipping notification.", offerId);
            return;
        }

        var activeHospitals = catalogGateway.findAllActiveHospitals();
        String subject = "Novo Insumo Disponível para Redistribuição - MediBridge";
        String body = String.format(
                "Olá,\n\nUm novo insumo está disponível para redistribuição no MediBridge!\n\n" +
                "Detalhes do insumo:\n" +
                "- Nome: %s\n" +
                "- Quantidade: %d %s\n\n" +
                "Acesse a plataforma do MediBridge para solicitar a transferência.\n\n" +
                "Atenciosamente,\nEquipe MediBridge",
                offer.medicineName(),
                offer.quantity(),
                offer.unit()
        );

        for (var hospital : activeHospitals) {
            if (!hospital.id().equals(publisherHospitalId)) {
                try {
                    notificationService.sendEmail(hospital.email(), subject, body);
                } catch (Exception e) {
                    log.error("Failed to send OfferCreated notification to hospital: {}", hospital.id(), e);
                }
            }
        }
    }
}

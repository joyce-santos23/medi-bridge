package br.com.medibridge.medi_bridge.notification.infra.integration;

import br.com.medibridge.medi_bridge.notification.core.application.dto.integration.NotificationOfferDetails;
import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationOfferGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OfferIntegrationAdapter implements NotificationOfferGateway {

    private final OfferRepositoryGateway offerRepositoryGateway;

    @Override
    public Optional<NotificationOfferDetails> findOfferById(UUID id) {
        return offerRepositoryGateway.findById(id)
                .map(offer -> new NotificationOfferDetails(
                        offer.getId(),
                        offer.getProduct().getName(),
                        offer.getProduct().getQuantity(),
                        offer.getProduct().getUnit().name(),
                        offer.getHospitalId()
                ));
    }
}

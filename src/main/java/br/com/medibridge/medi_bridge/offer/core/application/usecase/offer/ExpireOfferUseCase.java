package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
import br.com.medibridge.medi_bridge.offer.core.application.port.EventPublisherGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpireOfferUseCase {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final EventPublisherGateway eventPublisherGateway;

    public OfferResponseDTO execute(UUID offerId) {
        log.info("Executing ExpireOfferUseCase for offer ID: {}", offerId);


        if (offerId == null) {
            throw new ValidationException("Offer ID is required");
        }

        Offer offer = offerRepositoryGateway.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer not found"));

        offer.expire();

        Offer savedOffer = offerRepositoryGateway.save(offer);

        eventPublisherGateway.publish(offer.pullDomainEvents());

        log.info("Successfully expired offer with ID: {}", savedOffer.getId());
        return OfferResponseDTO.from(savedOffer);
    }
}

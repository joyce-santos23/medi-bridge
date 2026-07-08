package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponse;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.offer.core.application.port.EventPublisherGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
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
public class CompleteOfferUseCase {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final EventPublisherGateway eventPublisherGateway;

    public OfferResponse execute(AuthenticatedUser currentUser, UUID offerId) {
        log.info("Executing CompleteOfferUseCase for offer ID: {} by user ID: {}", offerId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (offerId == null) {
            throw new ValidationException("Offer ID is required");
        }

        Offer offer = offerRepositoryGateway.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer not found"));

        offer.complete();

        Offer savedOffer = offerRepositoryGateway.save(offer);

        eventPublisherGateway.publish(savedOffer.pullDomainEvents());

        log.info("Successfully completed offer with ID: {}", savedOffer.getId());
        return OfferResponse.from(savedOffer);
    }
}

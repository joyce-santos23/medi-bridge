package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
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
public class CancelOfferUseCase {

    private final OfferRepositoryGateway offerGateway;
    private final EventPublisherGateway eventPublisherGateway;

    public OfferResponseDTO execute(AuthenticatedUser currentUser, UUID offerId) {
        log.info("Executing CancelOfferUseCase for offer ID: {} by user ID: {}", offerId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (offerId == null) {
            throw new ValidationException("Offer ID is required");
        }

        Offer offer = offerGateway.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer not found"));

        if (!offer.getHospitalId().equals(currentUser.hospitalId())) {
            throw new ForbiddenException("You can only cancel offers belonging to your hospital");
        }

        offer.cancel();

        Offer savedOffer = offerGateway.save(offer);

        eventPublisherGateway.publish(offer.pullDomainEvents());

        log.info("Successfully cancelled offer with ID: {}", savedOffer.getId());
        return OfferResponseDTO.from(savedOffer);
    }
}

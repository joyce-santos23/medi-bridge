package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.offer.input.UpdateOfferInput;
import br.com.medibridge.medi_bridge.offer.core.application.dto.offer.output.OfferOutput;
import br.com.medibridge.medi_bridge.offer.core.application.port.EventPublisherGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.offer.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.offer.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.offer.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.offer.core.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.valueobject.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateOfferUseCase {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final EventPublisherGateway eventPublisherGateway;

    public OfferOutput execute(AuthenticatedUser currentUser, UpdateOfferInput input) {
        log.info("Executing UpdateOfferUseCase for offer ID: {} by user ID: {}", input != null ? input.id() : "null", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (input == null || input.id() == null || input.product() == null) {
            throw new ValidationException("Offer ID and product data are required");
        }

        Offer offer = offerRepositoryGateway.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Offer not found"));

        if (!offer.getHospitalId().equals(currentUser.hospitalId())) {
            throw new ForbiddenException("You can only update offers belonging to your hospital");
        }

        Product newProduct = new Product(
                input.product().name(),
                input.product().category(),
                input.product().manufacturer(),
                input.product().batch(),
                input.product().expirationDate(),
                input.product().quantity(),
                input.product().unit(),
                input.product().observations()
        );

        offer.update(newProduct);

        Offer savedOffer = offerRepositoryGateway.save(offer);

        eventPublisherGateway.publish(savedOffer.getDomainEvents());
        savedOffer.pullDomainEvents();

        log.info("Successfully updated offer with ID: {}", savedOffer.getId());
        return OfferOutput.from(savedOffer);
    }
}

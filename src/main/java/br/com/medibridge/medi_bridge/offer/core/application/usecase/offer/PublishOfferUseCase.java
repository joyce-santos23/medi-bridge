package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.offer.input.PublishOfferInput;
import br.com.medibridge.medi_bridge.offer.core.application.dto.offer.output.OfferOutput;
import br.com.medibridge.medi_bridge.offer.core.application.port.CatalogGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.EventPublisherGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.offer.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.offer.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.offer.core.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.valueobject.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishOfferUseCase {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final EventPublisherGateway eventPublisherGateway;
    private final CatalogGateway catalogGateway;

    public OfferOutput execute(AuthenticatedUser currentUser, PublishOfferInput input) {
        log.info("Executing PublishOfferUseCase for user ID: {}", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.hospitalId() == null) {
            throw new ValidationException("User must be associated with a hospital to publish an offer");
        }

        if (!catalogGateway.existsHospitalById(currentUser.hospitalId())) {
            throw new ValidationException("Associated hospital does not exist in catalog");
        }

        if (input == null || input.product() == null) {
            throw new ValidationException("Product data is required to publish an offer");
        }

        Product product = new Product(
                input.product().name(),
                input.product().category(),
                input.product().manufacturer(),
                input.product().batch(),
                input.product().expirationDate(),
                input.product().quantity(),
                input.product().unit(),
                input.product().observations()
        );

        Offer offer = Offer.publish(
                currentUser.hospitalId(),
                currentUser.id(),
                product
        );

        Offer savedOffer = offerRepositoryGateway.save(offer);

        eventPublisherGateway.publish(savedOffer.getDomainEvents());
        savedOffer.pullDomainEvents();

        log.info("Successfully published offer with ID: {}", savedOffer.getId());
        return OfferOutput.from(savedOffer);
    }
}

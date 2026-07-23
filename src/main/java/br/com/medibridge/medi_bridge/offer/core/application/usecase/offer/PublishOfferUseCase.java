package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.PublishOfferRequestDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.offer.core.application.port.*;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
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

    public OfferResponseDTO execute(AuthenticatedUser currentUser, PublishOfferRequestDTO request) {
        log.info("Executing PublishOfferUseCase for user ID: {}", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.hospitalId() == null) {
            throw new ValidationException("User must be associated with a hospital to publish an offer");
        }

        HospitalSummary hospital = catalogGateway.findHospitalById(currentUser.hospitalId())
                .orElseThrow(() -> new ValidationException("Associated hospital does not exist in catalog"));

        if (!hospital.active()) {
            throw new ValidationException("Associated hospital is not active");
        }

        if (request == null || request.product() == null) {
            throw new ValidationException("Product data is required to publish an offer");
        }

        PublishOfferRequestDTO.ProductRequestDTO prodReq = request.product();
        Product product = new Product(
                prodReq.name(),
                prodReq.category(),
                prodReq.manufacturer(),
                prodReq.batch(),
                prodReq.expirationDate(),
                prodReq.quantity(),
                prodReq.unit(),
                prodReq.observations()
        );

        Offer offer = Offer.publish(
                currentUser.hospitalId(),
                currentUser.id(),
                product
        );

        Offer savedOffer = offerRepositoryGateway.save(offer);

        eventPublisherGateway.publish(offer.pullDomainEvents());

        UserSummary creator = catalogGateway.findUserById(currentUser.id()).orElse(null);

        log.info("Successfully published offer with ID: {}", savedOffer.getId());
        return OfferResponseDTO.from(savedOffer, hospital, creator);
    }
}

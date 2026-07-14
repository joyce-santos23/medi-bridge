package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.UpdateOfferRequestDTO;
import br.com.medibridge.medi_bridge.offer.core.application.port.EventPublisherGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
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

    public OfferResponseDTO execute(AuthenticatedUser currentUser, UpdateOfferRequestDTO request) {
        log.info("Executing UpdateOfferUseCase for offer ID: {} by user ID: {}", request != null ? request.id() : "null", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (request == null || request.id() == null || request.product() == null) {
            throw new ValidationException("Offer ID and product data are required");
        }

        Offer offer = offerRepositoryGateway.findById(request.id())
                .orElseThrow(() -> new NotFoundException("Offer not found"));

        if (!offer.getHospitalId().equals(currentUser.hospitalId())) {
            throw new ForbiddenException("You can only update offers belonging to your hospital");
        }

        UpdateOfferRequestDTO.ProductRequestDTO prodReq = request.product();
        Product newProduct = new Product(
                prodReq.name(),
                prodReq.category(),
                prodReq.manufacturer(),
                prodReq.batch(),
                prodReq.expirationDate(),
                prodReq.quantity(),
                prodReq.unit(),
                prodReq.observations()
        );

        offer.update(newProduct);

        Offer savedOffer = offerRepositoryGateway.save(offer);

        eventPublisherGateway.publish(savedOffer.pullDomainEvents());

        log.info("Successfully updated offer with ID: {}", savedOffer.getId());
        return OfferResponseDTO.from(savedOffer);
    }
}

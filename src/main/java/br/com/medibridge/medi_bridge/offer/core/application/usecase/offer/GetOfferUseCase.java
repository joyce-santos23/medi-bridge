package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.offer.core.application.port.*;
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
public class GetOfferUseCase {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final CatalogGateway catalogGateway;

    public OfferResponseDTO execute(AuthenticatedUser currentUser, UUID offerId) {
        log.info("Executing GetOfferUseCase for offer ID: {} by user ID: {}", offerId, currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (offerId == null) {
            throw new ValidationException("Offer ID is required");
        }

        Offer offer = offerRepositoryGateway.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer not found"));

        HospitalSummary hospital = catalogGateway.findHospitalById(offer.getHospitalId()).orElse(null);
        UserSummary creator = catalogGateway.findUserById(offer.getCreatedByUserId()).orElse(null);

        return OfferResponseDTO.from(offer, hospital, creator);
    }
}

package br.com.medibridge.medi_bridge.offer.core.application.usecase.offer;

import br.com.medibridge.medi_bridge.offer.core.application.dto.OfferResponseDTO;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.offer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.offer.core.application.port.*;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListAvailableOffersUseCase {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final CatalogGateway catalogGateway;

    public List<OfferResponseDTO> execute(AuthenticatedUser currentUser) {
        log.info("Executing ListAvailableOffersUseCase by user ID: {}", currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        return offerRepositoryGateway.findAllAvailable().stream()
                .map(offer -> {
                    HospitalSummary hospital = catalogGateway.findHospitalById(offer.getHospitalId()).orElse(null);
                    UserSummary creator = catalogGateway.findUserById(offer.getCreatedByUserId()).orElse(null);
                    return OfferResponseDTO.from(offer, hospital, creator);
                })
                .toList();
    }
}

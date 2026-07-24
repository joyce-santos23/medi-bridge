package br.com.medibridge.medi_bridge.transfer.infra.integration;

import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.integration.CompleteOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.integration.ReopenOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.integration.ReserveOfferUseCase;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.OfferSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.ProductSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.port.OfferGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OfferAdapter implements OfferGateway {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final ReserveOfferUseCase reserveOfferUseCase;
    private final ReopenOfferUseCase reopenOfferUseCase;
    private final CompleteOfferUseCase completeOfferUseCase;

    @Override
    public Optional<OfferSummary> findById(UUID offerId) {
        return offerRepositoryGateway.findById(offerId)
                .map(offer -> {
                    ProductSummary productSummary = new ProductSummary(
                            offer.getProduct().getName(),
                            offer.getProduct().getCategory().name(),
                            offer.getProduct().getManufacturer(),
                            offer.getProduct().getBatch(),
                            offer.getProduct().getExpirationDate(),
                            offer.getProduct().getQuantity(),
                            offer.getProduct().getUnit().name(),
                            offer.getProduct().getObservations()
                    );

                    return new OfferSummary(
                            offer.getId(),
                            offer.getHospitalId(),
                            productSummary
                    );
                });
    }

    @Override
    public void reserve(UUID offerId) {
        reserveOfferUseCase.execute(offerId);
    }

    @Override
    public void release(UUID offerId) {
        reopenOfferUseCase.execute(offerId);
    }

    @Override
    public void complete(UUID offerId) {
        completeOfferUseCase.execute(offerId);
    }
}

package br.com.medibridge.medi_bridge.transfer.core.application.port;

import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.OfferSummary;

import java.util.Optional;
import java.util.UUID;

public interface OfferGateway {
    Optional<OfferSummary> findById(UUID offerId);
    void reserve(UUID offerId);
    void release(UUID offerId);
    void complete(UUID offerId);
}

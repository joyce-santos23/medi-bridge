package br.com.medibridge.medi_bridge.transfer.core.application.port;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface OfferGateway {
    Optional<OfferSummary> findById(UUID offerId);
    void reserve(UUID offerId);
    void release(UUID offerId);
    void complete(UUID offerId);

    record OfferSummary(
            UUID id,
            UUID hospitalId,
            UUID createdByUserId,
            String status,
            LocalDate expirationDate
    ) {}
}

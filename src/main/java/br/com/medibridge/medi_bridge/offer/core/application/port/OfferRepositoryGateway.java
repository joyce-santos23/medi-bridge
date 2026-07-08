package br.com.medibridge.medi_bridge.offer.core.application.port;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferRepositoryGateway {
    Offer save(Offer offer);
    Optional<Offer> findById(UUID id);
    List<Offer> findAllAvailable();
}

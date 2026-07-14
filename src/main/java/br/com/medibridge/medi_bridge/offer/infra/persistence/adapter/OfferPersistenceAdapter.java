package br.com.medibridge.medi_bridge.offer.infra.persistence.adapter;

import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import br.com.medibridge.medi_bridge.offer.infra.persistence.entity.OfferEntity;
import br.com.medibridge.medi_bridge.offer.infra.persistence.mapper.OfferPersistenceMapper;
import br.com.medibridge.medi_bridge.offer.infra.persistence.repository.OfferJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OfferPersistenceAdapter implements OfferRepositoryGateway {

    private final OfferJpaRepository repository;

    @Override
    public Offer save(Offer offer) {
        OfferEntity entity = OfferPersistenceMapper.toEntity(offer);
        OfferEntity saved = repository.save(entity);
        return OfferPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Offer> findById(UUID id) {
        return repository.findById(id)
                .map(OfferPersistenceMapper::toDomain);
    }

    @Override
    public List<Offer> findAllAvailable() {
        return repository.findAllByStatus(OfferStatus.AVAILABLE).stream()
                .map(OfferPersistenceMapper::toDomain)
                .toList();
    }
}

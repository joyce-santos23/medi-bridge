package br.com.medibridge.medi_bridge.offer.infra.persistence.repository;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import br.com.medibridge.medi_bridge.offer.infra.persistence.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferJpaRepository extends JpaRepository<OfferEntity, UUID> {
    List<OfferEntity> findAllByStatus(OfferStatus status);
}

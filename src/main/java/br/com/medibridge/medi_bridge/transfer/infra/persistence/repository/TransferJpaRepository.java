package br.com.medibridge.medi_bridge.transfer.infra.persistence.repository;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import br.com.medibridge.medi_bridge.transfer.infra.persistence.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransferJpaRepository extends JpaRepository<TransferEntity, UUID> {

    List<TransferEntity> findByStatusInAndExpiresAtBefore(List<TransferStatus> statuses, Instant time);

    List<TransferEntity> findBySourceHospitalIdOrDestinationHospitalId(UUID sourceHospitalId, UUID destinationHospitalId);

    List<TransferEntity> findByOfferId(UUID offerId);
}

package br.com.medibridge.medi_bridge.transfer.infra.persistence.adapter;

import br.com.medibridge.medi_bridge.transfer.core.application.port.TransferGateway;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import br.com.medibridge.medi_bridge.transfer.infra.persistence.entity.TransferEntity;
import br.com.medibridge.medi_bridge.transfer.infra.persistence.mapper.TransferPersistenceMapper;
import br.com.medibridge.medi_bridge.transfer.infra.persistence.repository.TransferJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransferPersistenceAdapter implements TransferGateway {

    private final TransferJpaRepository repository;

    @Override
    public Transfer save(Transfer transfer) {
        TransferEntity entity = TransferPersistenceMapper.toEntity(transfer);
        TransferEntity saved = repository.save(entity);
        return TransferPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Transfer> findById(UUID id) {
        return repository.findById(id)
                .map(TransferPersistenceMapper::toDomain);
    }

    @Override
    public List<Transfer> findAll() {
        return repository.findAll().stream()
                .map(TransferPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Transfer> findActiveTransfersBefore(Instant time) {
        return repository.findByStatusInAndExpiresAtBefore(
                List.of(TransferStatus.APPROVED),
                time
        ).stream()
                .map(TransferPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Transfer> findByHospital(UUID hospitalId) {
        return repository.findBySourceHospitalIdOrDestinationHospitalId(hospitalId, hospitalId).stream()
                .map(TransferPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Transfer> findByOfferId(UUID offerId) {
        return repository.findByOfferId(offerId).stream()
                .map(TransferPersistenceMapper::toDomain)
                .toList();
    }
}

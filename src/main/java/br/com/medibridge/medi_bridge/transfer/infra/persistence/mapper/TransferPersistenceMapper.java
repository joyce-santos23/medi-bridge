package br.com.medibridge.medi_bridge.transfer.infra.persistence.mapper;

import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import br.com.medibridge.medi_bridge.transfer.infra.persistence.entity.TransferEntity;

public final class TransferPersistenceMapper {

    private TransferPersistenceMapper() {
    }

    public static TransferEntity toEntity(Transfer domain) {
        if (domain == null) {
            return null;
        }
        TransferEntity entity = new TransferEntity();
        entity.setId(domain.getId());
        entity.setOfferId(domain.getOfferId());
        entity.setSourceHospitalId(domain.getSourceHospitalId());
        entity.setDestinationHospitalId(domain.getDestinationHospitalId());
        entity.setRequesterUserId(domain.getRequesterUserId());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setStatusChangedAt(domain.getStatusChangedAt());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setReason(domain.getReason());
        entity.setConfirmationCode(domain.getConfirmationCode());
        return entity;
    }

    public static Transfer toDomain(TransferEntity entity) {
        if (entity == null) {
            return null;
        }
        return Transfer.restore(
                entity.getId(),
                entity.getOfferId(),
                entity.getSourceHospitalId(),
                entity.getDestinationHospitalId(),
                entity.getRequesterUserId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getStatusChangedAt(),
                entity.getExpiresAt(),
                entity.getReason(),
                entity.getConfirmationCode()
        );
    }
}

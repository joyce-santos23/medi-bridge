package br.com.medibridge.medi_bridge.transfer.core.domain.event;

import java.time.Instant;
import java.util.UUID;

public record TransferCreated(
        UUID transferId,
        UUID offerId,
        UUID sourceHospitalId,
        UUID destinationHospitalId,
        Instant occurredOn
) implements DomainEvent {
    public TransferCreated(UUID transferId, UUID offerId, UUID sourceHospitalId, UUID destinationHospitalId) {
        this(transferId, offerId, sourceHospitalId, destinationHospitalId, Instant.now());
    }
}

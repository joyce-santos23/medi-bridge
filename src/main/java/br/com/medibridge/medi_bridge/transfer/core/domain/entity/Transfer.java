package br.com.medibridge.medi_bridge.transfer.core.domain.entity;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import br.com.medibridge.medi_bridge.transfer.core.domain.event.*;
import br.com.medibridge.medi_bridge.transfer.core.domain.exception.TransferValidationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transfer {

    @EqualsAndHashCode.Include
    private final UUID id;

    private final UUID offerId;
    private final UUID sourceHospitalId;
    private final UUID destinationHospitalId;
    private final UUID requesterUserId;
    private TransferStatus status;
    private final Instant createdAt;
    private Instant statusChangedAt;
    private Instant expiresAt;
    private String reason;
    private String confirmationCode;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Transfer(
            UUID id,
            UUID offerId,
            UUID sourceHospitalId,
            UUID destinationHospitalId,
            UUID requesterUserId,
            TransferStatus status,
            Instant createdAt,
            Instant statusChangedAt,
            Instant expiresAt,
            String reason,
            String confirmationCode
    ) {
        this.id = id;
        this.offerId = offerId;
        this.sourceHospitalId = sourceHospitalId;
        this.destinationHospitalId = destinationHospitalId;
        this.requesterUserId = requesterUserId;
        this.status = status;
        this.createdAt = createdAt;
        this.statusChangedAt = statusChangedAt;
        this.expiresAt = expiresAt;
        this.reason = reason;
        this.confirmationCode = confirmationCode;
    }

    /**
     * Factory utilizada para criação de uma nova transferência.
     */
    public static Transfer create(
            UUID offerId,
            UUID sourceHospitalId,
            UUID destinationHospitalId,
            UUID requesterUserId,
            Instant now
    ) {
        require(offerId, "Offer ID is required");
        require(sourceHospitalId, "Source hospital ID is required");
        require(destinationHospitalId, "Destination hospital ID is required");
        require(requesterUserId, "Requester user ID is required");
        require(now, "Current time is required");

        if (sourceHospitalId.equals(destinationHospitalId)) {
            throw new TransferValidationException("Source and destination hospitals must be different");
        }

        UUID transferId = UUID.randomUUID();

        Transfer transfer = new Transfer(
                transferId,
                offerId,
                sourceHospitalId,
                destinationHospitalId,
                requesterUserId,
                TransferStatus.PENDING_APPROVAL,
                now,
                now,
                null,
                null,
                null
        );

        transfer.registerEvent(new TransferCreated(transferId, offerId, sourceHospitalId, destinationHospitalId));

        return transfer;
    }

    /**
     * Factory utilizada pela camada de persistência para reconstruir o Aggregate.
     */
    public static Transfer restore(
            UUID id,
            UUID offerId,
            UUID sourceHospitalId,
            UUID destinationHospitalId,
            UUID requesterUserId,
            TransferStatus status,
            Instant createdAt,
            Instant statusChangedAt,
            Instant expiresAt,
            String reason,
            String confirmationCode
    ) {
        return new Transfer(
                id,
                offerId,
                sourceHospitalId,
                destinationHospitalId,
                requesterUserId,
                status,
                createdAt,
                statusChangedAt,
                expiresAt,
                reason,
                confirmationCode
        );
    }

    /**
     * PENDING_APPROVAL -> APPROVED
     */
    public void approve(Instant now) {
        require(now, "Current time is required");
        ensureStatus(TransferStatus.PENDING_APPROVAL);

        this.status = TransferStatus.APPROVED;
        this.expiresAt = now.plus(7, ChronoUnit.DAYS);
        this.confirmationCode = java.util.UUID.randomUUID()
                .toString().replace("-", "")
                .substring(0, 8).toUpperCase();
        touch(now);

        registerEvent(new TransferApproved(id));
    }

    /**
     * PENDING_APPROVAL -> REJECTED
     */
    public void reject(String reason, Instant now) {
        require(now, "Current time is required");
        if (reason == null || reason.trim().isEmpty()) {
            throw new TransferValidationException("Reason is required to reject a transfer");
        }
        ensureStatus(TransferStatus.PENDING_APPROVAL);

        if (isExpired(now)) {
            throw new TransferValidationException(
                    "Transfer has expired"
            );
        }

        this.status = TransferStatus.REJECTED;
        this.reason = reason;
        touch(now);

        registerEvent(new TransferRejected(id, reason));
    }

    /**
     * PENDING_APPROVAL -> CANCELLED
     * APPROVED         -> CANCELLED
     */
    public void cancel(String reason, Instant now) {
        require(now, "Current time is required");
        if (reason == null || reason.trim().isEmpty()) {
            throw new TransferValidationException("Reason is required to cancel a transfer");
        }
        ensureStatus(TransferStatus.PENDING_APPROVAL, TransferStatus.APPROVED);

        if (isExpired(now)) {
            throw new TransferValidationException(
                    "Transfer has expired"
            );
        }

        this.status = TransferStatus.CANCELLED;
        this.reason = reason;
        touch(now);

        registerEvent(new TransferCancelled(id, reason));
    }

    /**
     * APPROVED -> COMPLETED
     */
    public void complete(String confirmationCode, Instant now) {
        require(now, "Current time is required");
        ensureStatus(TransferStatus.APPROVED);

        if (this.confirmationCode == null || !this.confirmationCode.equals(confirmationCode)) {
            throw new TransferValidationException("Invalid confirmation code");
        }

        if (isExpired(now)) {
            throw new TransferValidationException(
                    "Transfer has expired"
            );
        }

        this.status = TransferStatus.COMPLETED;
        touch(now);

        registerEvent(new TransferCompleted(id));
    }

    /**
     * PENDING_APPROVAL -> EXPIRED
     * APPROVED         -> EXPIRED
     */
    public void expire(Instant now) {
        require(now, "Current time is required");
        ensureStatus(TransferStatus.PENDING_APPROVAL, TransferStatus.APPROVED);

        this.status = TransferStatus.EXPIRED;
        touch(now);

        registerEvent(new TransferExpired(id));
    }

    /**
     * Retorna todos os eventos registrados e limpa a lista interna.
     */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    public boolean isExpired(Instant now) {
        return expiresAt != null && now.isAfter(expiresAt);
    }

    // ==========================================================
    // Private Helpers
    // ==========================================================

    private void touch(Instant now) {
        this.statusChangedAt = now;
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    private void ensureStatus(TransferStatus... allowedStatuses) {
        for (TransferStatus allowed : allowedStatuses) {
            if (this.status == allowed) {
                return;
            }
        }
        throw new TransferValidationException(
                String.format(
                        "Operation is not allowed while Transfer is in status '%s'",
                        this.status
                )
        );
    }

    private static <T> T require(T value, String message) {
        if (value == null) {
            throw new TransferValidationException(message);
        }
        return value;
    }
}

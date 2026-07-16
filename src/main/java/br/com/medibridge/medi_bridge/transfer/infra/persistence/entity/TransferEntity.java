package br.com.medibridge.medi_bridge.transfer.infra.persistence.entity;

import br.com.medibridge.medi_bridge.transfer.core.domain.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntity {

    @Id
    private UUID id;

    @Column(name = "offer_id", nullable = false)
    private UUID offerId;

    @Column(name = "source_hospital_id", nullable = false)
    private UUID sourceHospitalId;

    @Column(name = "destination_hospital_id", nullable = false)
    private UUID destinationHospitalId;

    @Column(name = "requester_user_id", nullable = false)
    private UUID requesterUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "status_changed_at", nullable = false)
    private Instant statusChangedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "confirmation_code")
    private String confirmationCode;
}

package br.com.medibridge.medi_bridge.offer.infra.persistence.entity;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Category;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Unit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferEntity {

    @Id
    private UUID id;

    @Column(name = "hospital_id", nullable = false)
    private UUID hospitalId;

    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Flattened Product value object fields
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    private Category productCategory;

    @Column(name = "product_manufacturer")
    private String productManufacturer;

    @Column(name = "product_batch", nullable = false)
    private String productBatch;

    @Column(name = "product_expiration_date", nullable = false)
    private LocalDate productExpirationDate;

    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_unit", nullable = false)
    private Unit productUnit;

    @Column(name = "product_observations")
    private String productObservations;
}

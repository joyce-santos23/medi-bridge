package br.com.medibridge.medi_bridge.offer.core.domain.offer.entity;

import br.com.medibridge.medi_bridge.offer.core.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.OfferStatus;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.event.*;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.valueobject.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Offer {

    private static final int MINIMUM_VALIDITY_DAYS = 90;

    @EqualsAndHashCode.Include
    private final UUID id;

    private final UUID hospitalId;
    private final UUID createdByUserId;

    private Product product;
    private OfferStatus status;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Offer(
            UUID id,
            UUID hospitalId,
            UUID createdByUserId,
            Product product,
            OfferStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.createdByUserId = createdByUserId;
        this.product = product;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory utilizada para criação de uma nova oferta.
     */
    public static Offer publish(
            UUID hospitalId,
            UUID createdByUserId,
            Product product
    ) {

        require(hospitalId, "Hospital ID is required");
        require(createdByUserId, "Created by user ID is required");
        require(product, "Product is required");

        validateMinimumExpiration(product);

        UUID offerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Offer offer = new Offer(
                offerId,
                hospitalId,
                createdByUserId,
                product,
                OfferStatus.AVAILABLE,
                now,
                now
        );

        offer.registerEvent(new OfferCreated(offerId, hospitalId));

        return offer;
    }

    /**
     * Factory utilizada pela camada de persistência para reconstruir o Aggregate.
     */
    public static Offer restore(
            UUID id,
            UUID hospitalId,
            UUID createdByUserId,
            Product product,
            OfferStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        return new Offer(
                id,
                hospitalId,
                createdByUserId,
                product,
                status,
                createdAt,
                updatedAt
        );
    }

    /**
     * AVAILABLE -> AVAILABLE
     */
    public void update(Product newProduct) {

        ensureStatus(OfferStatus.AVAILABLE);

        require(newProduct, "Product is required");
        validateMinimumExpiration(newProduct);

        this.product = newProduct;

        touch();

        registerEvent(new OfferUpdated(id));
    }

    /**
     * AVAILABLE -> RESERVED
     */
    public void reserve() {

        ensureStatus(OfferStatus.AVAILABLE);

        validateMinimumExpiration(product);

        this.status = OfferStatus.RESERVED;

        touch();

        registerEvent(new OfferReserved(id));
    }

    /**
     * AVAILABLE -> CANCELLED
     * RESERVED  -> CANCELLED
     */
    public void cancel() {

        ensureStatus(
                OfferStatus.AVAILABLE,
                OfferStatus.RESERVED
        );

        this.status = OfferStatus.CANCELLED;

        touch();

        registerEvent(new OfferCancelled(id));
    }

    /**
     * RESERVED -> COMPLETED
     */
    public void complete() {

        ensureStatus(OfferStatus.RESERVED);

        this.status = OfferStatus.COMPLETED;

        touch();

        registerEvent(new OfferCompleted(id));
    }

    /**
     * RESERVED -> AVAILABLE
     * RESERVED -> EXPIRED
     *
     * Utilizado quando uma Transfer é rejeitada,
     * cancelada ou expira por timeout.
     */
    public void makeAvailableAgain() {

        ensureStatus(OfferStatus.RESERVED);

        touch();

        try {

            validateMinimumExpiration(product);

            this.status = OfferStatus.AVAILABLE;

            registerEvent(new OfferAvailable(id));

        } catch (ValidationException ex) {

            this.status = OfferStatus.EXPIRED;

            registerEvent(new OfferExpired(id));
        }
    }

    /**
     * AVAILABLE -> EXPIRED
     */
    public void expire() {

        ensureStatus(OfferStatus.AVAILABLE);

        this.status = OfferStatus.EXPIRED;

        touch();

        registerEvent(new OfferExpired(id));
    }

    /**
     * Retorna todos os eventos registrados
     * e limpa a lista interna.
     */
    public List<DomainEvent> pullDomainEvents() {

        List<DomainEvent> events = List.copyOf(domainEvents);

        domainEvents.clear();

        return events;
    }

    // ==========================================================
    // Private Helpers
    // ==========================================================

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    private void ensureStatus(OfferStatus... allowedStatuses) {

        for (OfferStatus allowed : allowedStatuses) {
            if (this.status == allowed) {
                return;
            }
        }

        throw new ValidationException(
                String.format(
                        "Operation is not allowed while Offer is in status '%s'",
                        this.status
                )
        );
    }

    private static void validateMinimumExpiration(Product product) {

        LocalDate minimumExpiration =
                LocalDate.now().plusDays(MINIMUM_VALIDITY_DAYS);

        if (product.getExpirationDate().isBefore(minimumExpiration)) {
            throw new ValidationException(
                    String.format(
                            "Product must have a minimum expiration validity of %d days",
                            MINIMUM_VALIDITY_DAYS
                    )
            );
        }
    }

    private static <T> T require(T value, String message) {

        if (value == null) {
            throw new ValidationException(message);
        }

        return value;
    }
}
package br.com.medibridge.medi_bridge.offer.core.domain.offer.valueobject;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Category;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Unit;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class Product {

    private final String name;
    private final Category category;
    private final String manufacturer;
    private final String batch;
    private final LocalDate expirationDate;
    private final Integer quantity;
    private final Unit unit;
    private final String observations;

    public Product(
            String name,
            Category category,
            String manufacturer,
            String batch,
            LocalDate expirationDate,
            Integer quantity,
            Unit unit,
            String observations
    ) {
        validate(name, category, batch, expirationDate, quantity, unit);
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.batch = batch;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.unit = unit;
        this.observations = observations;
    }

    private void validate(
            String name,
            Category category,
            String batch,
            LocalDate expirationDate,
            Integer quantity,
            Unit unit
    ) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Product name is required");
        }
        if (category == null) {
            throw new ValidationException("Product category is required");
        }
        if (batch == null || batch.isBlank()) {
            throw new ValidationException("Product batch is required");
        }
        if (expirationDate == null) {
            throw new ValidationException("Product expiration date is required");
        }
        if (quantity == null || quantity <= 0) {
            throw new ValidationException("Product quantity must be greater than zero");
        }
        if (unit == null) {
            throw new ValidationException("Product unit is required");
        }
    }
}

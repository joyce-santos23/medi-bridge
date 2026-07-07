package br.com.medibridge.medi_bridge.catalog.core.domain.address;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AddressBase {

    @EqualsAndHashCode.Include
    private final UUID id;
    private final String zipCode;
    private final String street;
    private final String neighborhood;
    private final String city;
    private final String state;

    private AddressBase(
            UUID id,
            String zipCode,
            String street,
            String neighborhood,
            String city,
            String state
    ) {
        this.id = require(id, "AddressBase id is required");
        this.zipCode = requireText(zipCode, "Zip code is required");
        this.street = requireText(street, "Street is required");
        this.neighborhood = requireText(neighborhood, "Neighborhood is required");
        this.city = requireText(city, "City is required");
        this.state = requireText(state, "State is required").toUpperCase();
    }

    public static AddressBase create(
            String zipCode,
            String street,
            String neighborhood,
            String city,
            String state
    ) {
        return new AddressBase(
                UUID.randomUUID(),
                zipCode,
                street,
                neighborhood,
                city,
                state
        );
    }

    public static AddressBase restore(
            UUID id,
            String zipCode,
            String street,
            String neighborhood,
            String city,
            String state
    ) {
        return new AddressBase(
                id,
                zipCode,
                street,
                neighborhood,
                city,
                state
        );
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
        return value.trim();
    }

    private static <T> T require(T value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
        return value;
    }
}

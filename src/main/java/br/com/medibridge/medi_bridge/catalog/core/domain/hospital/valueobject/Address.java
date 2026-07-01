package br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class Address {

    private final String street;
    private final String number;
    private final String complement;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String zipCode;

    private Address(
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            String zipCode
    ) {
        this.street = requireText(street, "Street is required");
        this.number = requireText(number, "Number is required");
        this.complement = normalize(complement);
        this.neighborhood = requireText(neighborhood, "Neighborhood is required");
        this.city = requireText(city, "City is required");
        this.state = requireText(state, "State is required").toUpperCase();
        this.zipCode = requireText(zipCode, "Zip code is required");
    }

    public static Address of(
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            String zipCode
    ) {
        return new Address(street, number, complement, neighborhood, city, state, zipCode);
    }

    private static String requireText(String value, String message) {
        String normalized = normalize(value);
        if (normalized == null) {
            throw new ValidationException(message);
        }
        return normalized;
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

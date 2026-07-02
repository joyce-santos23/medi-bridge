package br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
public final class Cnpj {

    private final String value;

    private Cnpj(String value) {
        this.value = normalize(value);
    }

    public static Cnpj of(String value) {
        return new Cnpj(value);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("CNPJ is required");
        }

        return value.replaceAll("\\D", "");
    }
}
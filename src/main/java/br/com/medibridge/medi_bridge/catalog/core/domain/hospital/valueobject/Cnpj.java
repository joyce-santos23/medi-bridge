package br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class Cnpj {

    private static final int LENGTH = 14;

    private final String value;

    private Cnpj(String value) {
        String digits = normalize(value);
        if (!isValid(digits)) {
            throw new ValidationException("Invalid CNPJ");
        }
        this.value = digits;
    }

    public static Cnpj of(String value) {
        return new Cnpj(value);
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\D", "");
    }

    private static boolean isValid(String digits) {
        if (digits.length() != LENGTH || digits.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateDigit(digits.substring(0, 12), new int[] {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int secondDigit = calculateDigit(digits.substring(0, 12) + firstDigit, new int[] {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        return Character.getNumericValue(digits.charAt(12)) == firstDigit
                && Character.getNumericValue(digits.charAt(13)) == secondDigit;
    }

    private static int calculateDigit(String base, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(base.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}

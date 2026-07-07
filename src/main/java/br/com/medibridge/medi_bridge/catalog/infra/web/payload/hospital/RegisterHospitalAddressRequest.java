package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterHospitalAddressRequest(
        @NotBlank
        @Pattern(regexp = "^\\d{8}$")
        String zipCode,

        @NotBlank
        @Size(max = 20)
        String number,

        @Size(max = 100)
        String complement
) {
}

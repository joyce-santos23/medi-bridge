package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateHospitalRequest(
        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 20)
        String phone,

        @NotNull
        Status status
) {
}

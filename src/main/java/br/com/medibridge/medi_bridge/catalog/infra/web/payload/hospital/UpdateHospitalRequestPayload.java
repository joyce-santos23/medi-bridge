package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateHospitalRequestPayload(
        @Email
        @Size(max = 100)
        String email,

        @Size(max = 20)
        String phone,

        Status status
) {
}

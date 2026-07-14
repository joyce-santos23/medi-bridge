package br.com.medibridge.medi_bridge.auth.infra.web.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestPayload(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}

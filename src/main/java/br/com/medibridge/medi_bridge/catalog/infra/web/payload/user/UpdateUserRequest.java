package br.com.medibridge.medi_bridge.catalog.infra.web.payload.user;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotNull
        Role role,

        @NotNull
        Status status
) {
}

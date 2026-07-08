package br.com.medibridge.medi_bridge.catalog.infra.web.payload.user;

import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100)
        String name,

        @Email
        @Size(max = 100)
        String email,

        Role role,

        Status status
) {
}

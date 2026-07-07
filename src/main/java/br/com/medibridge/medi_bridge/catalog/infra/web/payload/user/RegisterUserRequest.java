package br.com.medibridge.medi_bridge.catalog.infra.web.payload.user;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.enums.ProfessionalCouncil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RegisterUserRequest(
        @NotNull
        UUID hospitalId,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotNull
        ProfessionalCouncil council,

        @NotBlank
        @Size(max = 50)
        String professionalRegistration,

        @NotNull
        Role role,

        @NotBlank
        @Size(min = 6, max = 100)
        String password
) {
}

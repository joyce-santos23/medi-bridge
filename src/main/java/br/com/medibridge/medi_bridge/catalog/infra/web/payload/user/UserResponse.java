package br.com.medibridge.medi_bridge.catalog.infra.web.payload.user;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID hospitalId,
        String name,
        String email,
        ProfessionalCouncil council,
        String professionalRegistration,
        Role role,
        UserStatus status
) {
}

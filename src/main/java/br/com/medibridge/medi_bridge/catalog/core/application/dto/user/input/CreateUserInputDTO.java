package br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import java.util.UUID;

public record CreateUserInputDTO(
        UUID hospitalId,
        String name,
        String email,
        ProfessionalCouncil council,
        String professionalRegistration,
        Role role,
        String password
) {
}

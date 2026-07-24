package br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input;

import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import java.util.UUID;

public record UpdateUserInputDTO(
        UUID id,
        String name,
        String email,
        Role role,
        UserStatus status
) {
}

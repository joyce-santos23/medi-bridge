package br.com.medibridge.medi_bridge.shared.application.security;

import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import java.util.UUID;

public record AuthenticatedUser(
        UUID id,
        Role role,
        UUID hospitalId
) {
}

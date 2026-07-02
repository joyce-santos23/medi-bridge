package br.com.medibridge.medi_bridge.catalog.core.application.security;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import java.util.UUID;

public record AuthenticatedUser(
        UUID id,
        Role role,
        UUID hospitalId
) {
}

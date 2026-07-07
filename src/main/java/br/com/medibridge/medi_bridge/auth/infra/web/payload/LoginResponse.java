package br.com.medibridge.medi_bridge.auth.infra.web.payload;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponse;

public record LoginResponse(
        String token,
        UserResponse user
) {
}

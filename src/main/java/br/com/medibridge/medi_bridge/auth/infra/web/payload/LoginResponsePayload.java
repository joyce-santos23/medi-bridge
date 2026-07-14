package br.com.medibridge.medi_bridge.auth.infra.web.payload;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponsePayload;

public record LoginResponsePayload(
        String token,
        UserResponsePayload user
) {
}

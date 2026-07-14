package br.com.medibridge.medi_bridge.auth.core.application.dto.auth.output;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;

public record LoginOutputDTO(
        String token,
        UserOutputDTO user
) {
}

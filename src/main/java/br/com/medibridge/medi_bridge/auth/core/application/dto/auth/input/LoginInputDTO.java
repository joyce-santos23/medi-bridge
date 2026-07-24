package br.com.medibridge.medi_bridge.auth.core.application.dto.auth.input;

public record LoginInputDTO(
        String email,
        String password
) {
}

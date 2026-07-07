package br.com.medibridge.medi_bridge.auth.core.application.dto.auth.input;

public record LoginInput(
        String email,
        String password
) {
}

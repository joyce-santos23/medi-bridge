package br.com.medibridge.medi_bridge.auth.core.application.port.security;

public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}

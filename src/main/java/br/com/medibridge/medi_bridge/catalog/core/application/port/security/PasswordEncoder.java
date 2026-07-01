package br.com.medibridge.medi_bridge.catalog.core.application.port.security;

public interface PasswordEncoder {

    String encode(String rawPassword);
}

package br.com.medibridge.medi_bridge.auth.core.application.port.security;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import java.util.Optional;

public interface TokenService {

    String generateToken(AuthenticatedUser user);

    Optional<AuthenticatedUser> validateToken(String token);
}

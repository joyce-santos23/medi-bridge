package br.com.medibridge.medi_bridge.auth.infra.security;

import br.com.medibridge.medi_bridge.auth.core.application.port.security.TokenService;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenServiceAdapter implements TokenService {

    @Value("${security.jwt.secret:my-secret-key-that-is-very-long-and-secure}")
    private String secret;

    @Value("${security.jwt.expiration-hours:24}")
    private long expirationHours;

    @Override
    public String generateToken(AuthenticatedUser user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("medi-bridge")
                .withSubject(user.id().toString())
                .withClaim("role", user.role().name())
                .withClaim("hospitalId", user.hospitalId().toString())
                .withExpiresAt(Instant.now().plus(expirationHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    @Override
    public Optional<AuthenticatedUser> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("medi-bridge")
                    .build()
                    .verify(token);

            UUID userId = UUID.fromString(decodedJWT.getSubject());
            Role role = Role.valueOf(decodedJWT.getClaim("role").asString());
            UUID hospitalId = UUID.fromString(decodedJWT.getClaim("hospitalId").asString());

            return Optional.of(new AuthenticatedUser(userId, role, hospitalId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

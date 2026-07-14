package br.com.medibridge.medi_bridge.auth.core.application.usecase;

import br.com.medibridge.medi_bridge.auth.core.application.dto.auth.input.LoginInputDTO;
import br.com.medibridge.medi_bridge.auth.core.application.dto.auth.output.LoginOutputDTO;
import br.com.medibridge.medi_bridge.auth.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.auth.core.application.port.security.TokenService;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginOutputDTO execute(LoginInputDTO input) {
        log.info("Attempting login for email: {}", input.email());

        User user = userGateway.findByEmail(input.email())
                .orElseThrow(() -> {
                    log.warn("Login failed: email {} not found", input.email());
                    return new ValidationException("Invalid email or password");
                });

        if (user.getStatus() != br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus.ACTIVE) {
            log.warn("Login failed: user {} is inactive", input.email());
            throw new ValidationException("User account is inactive");
        }

        if (!passwordEncoder.matches(input.password(), user.getPasswordHash())) {
            log.warn("Login failed: incorrect password for email {}", input.email());
            throw new ValidationException("Invalid email or password");
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                user.getId(),
                user.getRole(),
                user.getHospitalId()
        );

        String token = tokenService.generateToken(authenticatedUser);
        log.info("Login successful for email: {}, generated token", input.email());

        return new LoginOutputDTO(token, UserOutputDTO.from(user));
    }
}

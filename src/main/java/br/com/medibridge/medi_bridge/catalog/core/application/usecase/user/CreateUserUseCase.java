package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.CreateUserInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.auth.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.shared.domain.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserUseCase {

    private final HospitalGateway hospitalGateway;
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public UserOutputDTO execute(AuthenticatedUser currentUser, CreateUserInputDTO input) {
        log.info("Executing CreateUserUseCase for email: {} and hospital ID: {} by user: {}", input.email(), input.hospitalId(), currentUser != null ? currentUser.id() : "anonymous");
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.role() != Role.ADMIN || !input.hospitalId().equals(currentUser.hospitalId())) {
            log.warn("Access denied for user ID: {} attempting to create user under hospital ID: {}", currentUser.id(), input.hospitalId());
            throw new ForbiddenException("Only administrators of this hospital can create users");
        }

        Hospital hospital = hospitalGateway.findById(input.hospitalId())
                .orElseThrow(() -> {
                    log.warn("Hospital with ID: {} not found during user creation", input.hospitalId());
                    return new NotFoundException("Hospital not found");
                });

        if (!hospital.isActive()) {
            log.warn("Attempted to create user for inactive hospital ID: {}", hospital.getId());
            throw new ValidationException("Inactive hospital cannot operate");
        }

        if (userGateway.findByEmail(input.email()).isPresent()) {
            log.warn("Attempted to create user with already registered email: {}", input.email());
            throw new DuplicateResourceException("email", "Email already registered");
        }

        User user = User.create(
                hospital.getId(),
                input.name(),
                input.email(),
                input.council(),
                input.professionalRegistration(),
                input.role(),
                passwordEncoder.encode(input.password())
        );

        User savedUser = userGateway.save(user);
        log.info("Successfully created user with ID: {} and role: {} for hospital ID: {}", savedUser.getId(), savedUser.getRole(), hospital.getId());
        return UserOutputDTO.from(savedUser);
    }
}

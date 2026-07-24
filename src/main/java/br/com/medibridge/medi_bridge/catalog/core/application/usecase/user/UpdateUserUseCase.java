package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.UpdateUserInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;
import br.com.medibridge.medi_bridge.auth.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public UserOutputDTO execute(AuthenticatedUser currentUser, UpdateUserInputDTO input) {
        log.info("Executing UpdateUserUseCase for user ID: {} by user ID: {}", input.id(), currentUser != null ? currentUser.id() : "anonymous");
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        User user = userGateway.findById(input.id())
                .orElseThrow(() -> {
                    log.warn("User with ID: {} not found during update profile", input.id());
                    return new NotFoundException("User not found");
                });

        boolean isSelf = currentUser.id().equals(user.getId());
        boolean isAdminOfSameHospital = currentUser.role() == Role.ADMIN 
                && currentUser.hospitalId().equals(user.getHospitalId());

        if (!isSelf && !isAdminOfSameHospital) {
            log.warn("Access denied for user ID: {} attempting to update user profile ID: {}", currentUser.id(), input.id());
            throw new ForbiddenException("You can only update your own user profile or you must be an admin of the same hospital");
        }

        user.update(
                input.name(),
                input.email(),
                input.role(),
                input.status()
        );

        User updatedUser = userGateway.save(user);
        log.info("Successfully updated user profile for ID: {}", input.id());
        return UserOutputDTO.from(updatedUser);
    }
}

package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserByIdUseCase {

    private final UserGateway userGateway;

    public UserOutput execute(AuthenticatedUser currentUser, UUID id) {
        log.info("Executing GetUserByIdUseCase for user ID: {} by requester ID: {}", id, currentUser != null ? currentUser.id() : "anonymous");
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        User user = userGateway.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID: {} not found", id);
                    return new NotFoundException("User not found");
                });

        if (!currentUser.id().equals(id) && 
            (currentUser.role() != Role.ADMIN || !user.getHospitalId().equals(currentUser.hospitalId()))) {
            log.warn("Access denied for user ID: {} attempting to get user profile ID: {}", currentUser.id(), id);
            throw new ForbiddenException("Access denied to this user profile");
        }

        log.info("Successfully retrieved user details for user ID: {}", id);
        return UserOutput.from(user);
    }
}

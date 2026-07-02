package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserGateway userGateway;

    public UserOutput execute(AuthenticatedUser currentUser, UUID id) {
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        User user = userGateway.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!currentUser.id().equals(id) && 
            (currentUser.role() != Role.ADMIN || !user.getHospitalId().equals(currentUser.hospitalId()))) {
            throw new ForbiddenException("Access denied to this user profile");
        }

        return UserOutput.from(user);
    }
}

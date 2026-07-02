package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.UpdateUserInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public UserOutput execute(AuthenticatedUser currentUser, UpdateUserInput input) {
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (!currentUser.id().equals(input.id())) {
            throw new ForbiddenException("You can only update your own user profile");
        }

        User user = userGateway.findById(input.id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.update(
                input.name(),
                input.email(),
                input.role(),
                input.status()
        );

        return UserOutput.from(userGateway.save(user));
    }
}

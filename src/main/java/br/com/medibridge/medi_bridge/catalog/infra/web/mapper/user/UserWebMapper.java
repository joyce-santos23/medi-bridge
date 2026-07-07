package br.com.medibridge.medi_bridge.catalog.infra.web.mapper.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.CreateUserInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.UpdateUserInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.RegisterUserRequest;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UpdateUserRequest;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponse;
import java.util.UUID;

public class UserWebMapper {

    public static CreateUserInput toInput(RegisterUserRequest request) {
        if (request == null) {
            return null;
        }
        return new CreateUserInput(
                request.hospitalId(),
                request.name(),
                request.email(),
                ProfessionalCouncil.valueOf(request.council().name()),
                request.professionalRegistration(),
                request.role(),
                request.password()
        );
    }

    public static UpdateUserInput toInput(UUID id, UpdateUserRequest request) {
        if (request == null) {
            return null;
        }
        return new UpdateUserInput(
                id,
                request.name(),
                request.email(),
                request.role(),
                request.status() != null ? UserStatus.valueOf(request.status().name()) : null
        );
    }

    public static UserResponse toResponse(UserOutput output) {
        if (output == null) {
            return null;
        }
        return new UserResponse(
                output.id(),
                output.hospitalId(),
                output.name(),
                output.email(),
                output.council(),
                output.professionalRegistration(),
                output.role(),
                output.status()
        );
    }
}

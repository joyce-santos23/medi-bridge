package br.com.medibridge.medi_bridge.catalog.infra.web.mapper.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.CreateUserInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.UpdateUserInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.RegisterUserRequestPayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UpdateUserRequestPayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponsePayload;
import java.util.UUID;

public final class UserWebMapper {

    private UserWebMapper() {
    }

    public static CreateUserInputDTO toInput(RegisterUserRequestPayload request) {
        if (request == null) {
            return null;
        }
        return new CreateUserInputDTO(
                request.hospitalId(),
                request.name(),
                request.email(),
                ProfessionalCouncil.valueOf(request.council().name()),
                request.professionalRegistration(),
                request.role(),
                request.password()
        );
    }

    public static UpdateUserInputDTO toInput(UUID id, UpdateUserRequestPayload request) {
        if (request == null) {
            return null;
        }
        return new UpdateUserInputDTO(
                id,
                request.name(),
                request.email(),
                request.role(),
                request.status() != null ? UserStatus.valueOf(request.status().name()) : null
        );
    }

    public static UserResponsePayload toResponse(UserOutputDTO output) {
        if (output == null) {
            return null;
        }
        return new UserResponsePayload(
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

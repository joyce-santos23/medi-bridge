package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;

public final class UserUseCaseMapper {

    private UserUseCaseMapper() {
    }

    public static UserOutput toOutput(User user) {
        return new UserOutput(
                user.getId(),
                user.getHospitalId(),
                user.getName(),
                user.getEmail(),
                user.getCouncil(),
                user.getProfessionalRegistration(),
                user.getRole(),
                user.getStatus()
        );
    }
}

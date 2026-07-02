package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.CreateUserInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.catalog.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final HospitalGateway hospitalGateway;
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public UserOutput execute(AuthenticatedUser currentUser, CreateUserInput input) {
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.role() != Role.ADMIN || !input.hospitalId().equals(currentUser.hospitalId())) {
            throw new ForbiddenException("Only administrators of this hospital can create users");
        }

        Hospital hospital = hospitalGateway.findById(input.hospitalId())
                .orElseThrow(() -> new NotFoundException("Hospital not found"));

        if (!hospital.isActive()) {
            throw new ValidationException("Inactive hospital cannot operate");
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

        return UserOutput.from(userGateway.save(user));
    }
}

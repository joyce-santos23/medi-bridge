package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.CreateUserInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalRepository;
import br.com.medibridge.medi_bridge.catalog.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserRepository;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserOutput execute(CreateUserInput input) {
        Hospital hospital = hospitalRepository.findById(input.hospitalId())
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

        return UserUseCaseMapper.toOutput(userRepository.save(user));
    }
}

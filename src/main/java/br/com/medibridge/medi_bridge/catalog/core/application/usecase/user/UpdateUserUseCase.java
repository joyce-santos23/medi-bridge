package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.input.UpdateUserInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserRepository;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserOutput execute(UpdateUserInput input) {
        User user = userRepository.findById(input.id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.update(
                input.name(),
                input.email(),
                input.council(),
                input.professionalRegistration(),
                input.role(),
                input.status(),
                passwordEncoder.encode(input.password())
        );

        return UserUseCaseMapper.toOutput(userRepository.save(user));
    }
}

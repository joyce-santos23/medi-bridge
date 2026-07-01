package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserRepository;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;

    public UserOutput execute(UUID id) {
        return userRepository.findById(id)
                .map(UserUseCaseMapper::toOutput)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

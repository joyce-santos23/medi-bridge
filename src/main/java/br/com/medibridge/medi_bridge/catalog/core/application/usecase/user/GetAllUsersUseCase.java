package br.com.medibridge.medi_bridge.catalog.core.application.usecase.user;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllUsersUseCase {

    private final UserGateway userGateway;

    public List<UserOutputDTO> execute(AuthenticatedUser currentUser) {
        log.info("Executing GetAllUsersUseCase for hospital ID: {}", currentUser != null ? currentUser.hospitalId() : "anonymous");
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        return userGateway.findAllByHospitalId(currentUser.hospitalId())
                .stream()
                .map(UserOutputDTO::from)
                .toList();
    }
}

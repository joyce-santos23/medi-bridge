package br.com.medibridge.medi_bridge.catalog.core.application.port.user;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);
}

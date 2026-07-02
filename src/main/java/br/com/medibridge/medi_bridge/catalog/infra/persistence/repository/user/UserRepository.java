package br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.user;

import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}

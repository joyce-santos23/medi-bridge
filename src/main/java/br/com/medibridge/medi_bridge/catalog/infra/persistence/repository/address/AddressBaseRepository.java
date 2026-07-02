package br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.address;

import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address.AddressBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressBaseRepository extends JpaRepository<AddressBaseEntity, UUID> {
    Optional<AddressBaseEntity> findByZipCode(String zipCode);
}

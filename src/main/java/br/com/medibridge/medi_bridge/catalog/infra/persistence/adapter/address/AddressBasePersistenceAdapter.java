package br.com.medibridge.medi_bridge.catalog.infra.persistence.adapter.address;

import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.entity.AddressBase;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address.AddressBaseEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.mapper.address.AddressBasePersistenceMapper;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.address.AddressBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddressBasePersistenceAdapter implements AddressBaseGateway {

    private final AddressBaseRepository repository;

    @Override
    public Optional<AddressBase> findByZipCode(String zipCode) {
        return repository.findByZipCode(zipCode)
                .map(AddressBasePersistenceMapper::toDomain);
    }

    @Override
    public Optional<AddressBase> findById(UUID id) {
        return repository.findById(id)
                .map(AddressBasePersistenceMapper::toDomain);
    }

    @Override
    public AddressBase save(AddressBase addressBase) {

        AddressBaseEntity entity = AddressBasePersistenceMapper.toEntity(addressBase);

        AddressBaseEntity saved = repository.save(entity);

        return AddressBasePersistenceMapper.toDomain(saved);
    }
}
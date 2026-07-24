package br.com.medibridge.medi_bridge.catalog.infra.persistence.adapter.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address.AddressBaseEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital.HospitalEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.mapper.hospital.HospitalPersistenceMapper;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.address.AddressBaseRepository;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.hospital.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HospitalPersistenceAdapter implements HospitalGateway {

    private final HospitalRepository repository;
    private final AddressBaseRepository addressBaseRepository;

    @Override
    public Hospital save(Hospital hospital) {

        Optional<HospitalEntity> existing = repository.findById(hospital.getId());

        HospitalEntity entity;

        if (existing.isPresent()) {

            entity = existing.get();

            HospitalPersistenceMapper.updateEntity(entity, hospital);

        } else {

            AddressBaseEntity addressBase =
                    addressBaseRepository.getReferenceById(hospital.getAddressBaseId());

            entity = HospitalPersistenceMapper.toEntity(
                    hospital,
                    addressBase
            );
        }

        HospitalEntity saved = repository.save(entity);

        return HospitalPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Hospital> findById(UUID id) {
        return repository.findById(id)
                .map(HospitalPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByCnpj(Cnpj cnpj) {
        return repository.existsByCnpj(cnpj.getValue());
    }

    @Override
    public boolean existsByCnpjAndIdNot(Cnpj cnpj, UUID id) {
        return repository.existsByCnpjAndIdNot(cnpj.getValue(), id);
    }

    @Override
    public boolean existsByCnes(String cnes) {
        return repository.existsByCnes(cnes);
    }

    @Override
    public boolean existsByCnesAndIdNot(String cnes, UUID id) {
        return repository.existsByCnesAndIdNot(cnes, id);
    }

    @Override
    public List<Hospital> findAllActive() {
        return repository.findByStatus(HospitalStatus.ACTIVE).stream()
                .map(HospitalPersistenceMapper::toDomain)
                .toList();
    }
}
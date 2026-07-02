package br.com.medibridge.medi_bridge.catalog.infra.persistence.mapper.hospital;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address.AddressBaseEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital.HospitalEntity;

public final class HospitalPersistenceMapper {

    private HospitalPersistenceMapper() {
    }

    public static HospitalEntity toEntity(Hospital domain, AddressBaseEntity addressBase) {

        if (domain == null) {
            return null;
        }

        HospitalEntity entity = new HospitalEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setCnpj(domain.getCnpj().getValue());
        entity.setCnes(domain.getCnes());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setAddressBase(addressBase);
        entity.setNumber(domain.getNumber());
        entity.setComplement(domain.getComplement());
        entity.setStatus(domain.getStatus());

        return entity;
    }

    public static void updateEntity(
            HospitalEntity entity,
            Hospital domain
    ) {
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setStatus(domain.getStatus());
    }

    public static Hospital toDomain(HospitalEntity entity) {

        if (entity == null) {
            return null;
        }

        return Hospital.restore(
                entity.getId(),
                entity.getName(),
                Cnpj.of(entity.getCnpj()),
                entity.getCnes(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddressBase().getId(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getStatus()
        );
    }
}
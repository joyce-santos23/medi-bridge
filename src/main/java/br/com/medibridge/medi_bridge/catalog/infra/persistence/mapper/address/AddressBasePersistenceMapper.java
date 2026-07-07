package br.com.medibridge.medi_bridge.catalog.infra.persistence.mapper.address;

import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address.AddressBaseEntity;

public final class AddressBasePersistenceMapper {

    private AddressBasePersistenceMapper() {
    }

    public static AddressBaseEntity toEntity(AddressBase domain) {
        if (domain == null) {
            return null;
        }

        AddressBaseEntity entity = new AddressBaseEntity();
        entity.setId(domain.getId());
        entity.setZipCode(domain.getZipCode());
        entity.setStreet(domain.getStreet());
        entity.setNeighborhood(domain.getNeighborhood());
        entity.setCity(domain.getCity());
        entity.setState(domain.getState());

        return entity;
    }

    public static AddressBase toDomain(AddressBaseEntity db) {
        if (db == null) {
            return null;
        }
        return AddressBase.restore(
                db.getId(),
                db.getZipCode(),
                db.getStreet(),
                db.getNeighborhood(),
                db.getCity(),
                db.getState()
        );
    }
}

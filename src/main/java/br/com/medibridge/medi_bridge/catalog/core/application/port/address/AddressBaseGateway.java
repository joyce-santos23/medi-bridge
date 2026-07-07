package br.com.medibridge.medi_bridge.catalog.core.application.port.address;

import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import java.util.Optional;
import java.util.UUID;

public interface AddressBaseGateway {

    Optional<AddressBase> findByZipCode(String zipCode);

    Optional<AddressBase> findById(UUID id);

    AddressBase save(AddressBase addressBase);
}

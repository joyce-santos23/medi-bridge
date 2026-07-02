package br.com.medibridge.medi_bridge.catalog.core.application.port.address;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutput;
import java.util.Optional;

public interface ViaCepGateway {

    Optional<ViaCepAddressOutput> findByZipCode(String zipCode);
}

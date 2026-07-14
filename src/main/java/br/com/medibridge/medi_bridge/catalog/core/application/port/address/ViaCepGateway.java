package br.com.medibridge.medi_bridge.catalog.core.application.port.address;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutputDTO;
import java.util.Optional;

public interface ViaCepGateway {

    Optional<ViaCepAddressOutputDTO> findByZipCode(String zipCode);
}

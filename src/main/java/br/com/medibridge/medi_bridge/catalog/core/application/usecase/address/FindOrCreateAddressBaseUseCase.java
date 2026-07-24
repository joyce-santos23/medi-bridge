package br.com.medibridge.medi_bridge.catalog.core.application.usecase.address;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.ViaCepGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindOrCreateAddressBaseUseCase {

    private final AddressBaseGateway addressBaseGateway;
    private final ViaCepGateway viaCepGateway;

    public AddressBase execute(String zipCode) {
        log.info("Executing FindOrCreateAddressBaseUseCase for zip code: {}", zipCode);
        return addressBaseGateway.findByZipCode(zipCode)
                .orElseGet(() -> {
                    log.info("Address details not found in local database for zip code: {}. Querying external ViaCEP service.", zipCode);
                    ViaCepAddressOutputDTO viaCepAddress = viaCepGateway.findByZipCode(zipCode)
                            .orElseThrow(() -> {
                                log.error("Address details not found in external ViaCEP service for zip code: {}", zipCode);
                                return new NotFoundException("Address details not found for zip code: " + zipCode);
                            });

                    AddressBase newAddress = AddressBase.create(
                            viaCepAddress.zipCode(),
                            viaCepAddress.street(),
                            viaCepAddress.neighborhood(),
                            viaCepAddress.city(),
                            viaCepAddress.state()
                    );
                    AddressBase savedAddress = addressBaseGateway.save(newAddress);
                    log.info("Saved new address details to database for zip code: {}, ID: {}", zipCode, savedAddress.getId());
                    return savedAddress;
                });
    }
}

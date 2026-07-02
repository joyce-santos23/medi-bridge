package br.com.medibridge.medi_bridge.catalog.core.application.usecase.address;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.ViaCepGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.entity.AddressBase;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindOrCreateAddressBaseUseCase {

    private final AddressBaseGateway addressBaseGateway;
    private final ViaCepGateway viaCepGateway;

    public AddressBase execute(String zipCode) {
        return addressBaseGateway.findByZipCode(zipCode)
                .orElseGet(() -> {
                    ViaCepAddressOutput viaCepAddress = viaCepGateway.findByZipCode(zipCode)
                            .orElseThrow(() -> new NotFoundException("Address details not found for zip code: " + zipCode));

                    AddressBase newAddress = AddressBase.create(
                            viaCepAddress.zipCode(),
                            viaCepAddress.street(),
                            viaCepAddress.neighborhood(),
                            viaCepAddress.city(),
                            viaCepAddress.state()
                    );
                    return addressBaseGateway.save(newAddress);
                });
    }
}

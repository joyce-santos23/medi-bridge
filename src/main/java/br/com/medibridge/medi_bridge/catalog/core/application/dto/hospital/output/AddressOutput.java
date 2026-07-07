package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;

public record AddressOutput(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
    public static AddressOutput from(AddressBase addressBase, String number, String complement) {
        if (addressBase == null) {
            return null;
        }
        return new AddressOutput(
                addressBase.getStreet(),
                number,
                complement,
                addressBase.getNeighborhood(),
                addressBase.getCity(),
                addressBase.getState(),
                addressBase.getZipCode()
        );
    }
}

package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

public record AddressOutput(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}

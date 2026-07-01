package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input;

public record AddressInput(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}

package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input;

public record AddressInput(
        String zipCode,
        String number,
        String complement
) {
}

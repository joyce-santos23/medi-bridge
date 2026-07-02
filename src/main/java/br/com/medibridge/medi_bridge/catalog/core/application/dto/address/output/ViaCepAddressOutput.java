package br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output;

public record ViaCepAddressOutput(
        String zipCode,
        String street,
        String neighborhood,
        String city,
        String state
) {
}

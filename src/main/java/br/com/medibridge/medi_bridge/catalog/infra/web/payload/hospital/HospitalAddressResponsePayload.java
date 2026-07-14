package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

public record HospitalAddressResponsePayload(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}

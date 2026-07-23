package br.com.medibridge.medi_bridge.offer.infra.web.payload.response;

public record HospitalAddressResponsePayload(
        String zipCode,
        String street,
        String neighborhood,
        String city,
        String state,
        String number,
        String complement
) {}

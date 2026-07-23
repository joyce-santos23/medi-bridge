package br.com.medibridge.medi_bridge.offer.infra.web.payload.response;

import java.util.UUID;

public record HospitalSummaryResponsePayload(
        UUID id,
        String name,
        String email,
        String phone,
        HospitalAddressResponsePayload address
) {}

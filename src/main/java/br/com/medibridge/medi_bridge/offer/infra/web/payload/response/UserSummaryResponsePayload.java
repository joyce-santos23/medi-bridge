package br.com.medibridge.medi_bridge.offer.infra.web.payload.response;

import java.util.UUID;

public record UserSummaryResponsePayload(
        UUID id,
        String name,
        String professionalCouncil,
        String professionalRegistration
) {}

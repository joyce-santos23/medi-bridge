package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponsePayload;

public record RegisterHospitalResponsePayload(
        HospitalResponsePayload hospital,
        UserResponsePayload adminUser
) {
}

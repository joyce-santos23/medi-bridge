package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponse;

public record RegisterHospitalResponse(
        HospitalResponse hospital,
        UserResponse adminUser
) {
}

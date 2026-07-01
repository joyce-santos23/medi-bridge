package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;

public record RegisterHospitalOutput(
        HospitalOutput hospital,
        UserOutput adminUser
) {
}

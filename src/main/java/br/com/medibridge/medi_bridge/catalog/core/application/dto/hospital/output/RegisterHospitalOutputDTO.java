package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutputDTO;

public record RegisterHospitalOutputDTO(
        HospitalOutputDTO hospital,
        UserOutputDTO adminUser
) {
}

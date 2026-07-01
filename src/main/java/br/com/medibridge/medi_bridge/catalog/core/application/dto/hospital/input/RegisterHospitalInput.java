package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;

public record RegisterHospitalInput(
        String name,
        String cnpj,
        String cnes,
        String email,
        String phone,
        AddressInput address,
        String adminName,
        String adminEmail,
        ProfessionalCouncil adminCouncil,
        String adminProfessionalRegistration,
        String adminPassword
) {
}

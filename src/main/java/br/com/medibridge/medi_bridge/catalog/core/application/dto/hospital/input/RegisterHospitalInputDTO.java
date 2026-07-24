package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;

public record RegisterHospitalInputDTO(
        String name,
        String cnpj,
        String cnes,
        String email,
        String phone,
        AddressInputDTO address,
        String adminName,
        String adminEmail,
        ProfessionalCouncil adminCouncil,
        String adminProfessionalRegistration,
        String adminPassword
) {
}

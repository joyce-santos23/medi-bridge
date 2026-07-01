package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import java.util.UUID;

public record UpdateHospitalInput(
        UUID id,
        String name,
        String cnpj,
        String cnes,
        String email,
        String phone,
        AddressInput address,
        HospitalStatus status
) {
}

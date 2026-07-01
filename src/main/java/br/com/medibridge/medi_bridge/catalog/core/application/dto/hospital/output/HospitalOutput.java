package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import java.util.UUID;

public record HospitalOutput(
        UUID id,
        String name,
        String cnpj,
        String cnes,
        String email,
        String phone,
        AddressOutput address,
        HospitalStatus status
) {
}

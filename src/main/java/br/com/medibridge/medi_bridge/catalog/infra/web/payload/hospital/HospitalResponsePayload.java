package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import java.util.UUID;

public record HospitalResponsePayload(
        UUID id,
        String name,
        String cnpj,
        String cnes,
        String email,
        String phone,
        HospitalAddressResponsePayload address,
        HospitalStatus status
) {
}

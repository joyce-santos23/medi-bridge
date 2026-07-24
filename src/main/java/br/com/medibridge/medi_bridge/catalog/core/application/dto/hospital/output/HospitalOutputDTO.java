package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import java.util.UUID;

public record HospitalOutputDTO(
        UUID id,
        String name,
        String cnpj,
        String cnes,
        String email,
        String phone,
        AddressOutputDTO address,
        HospitalStatus status
) {
    public static HospitalOutputDTO from(Hospital hospital, AddressBase addressBase) {
        if (hospital == null) {
            return null;
        }
        return new HospitalOutputDTO(
                hospital.getId(),
                hospital.getName(),
                hospital.getCnpj().getValue(),
                hospital.getCnes(),
                hospital.getEmail(),
                hospital.getPhone(),
                AddressOutputDTO.from(addressBase, hospital.getNumber(), hospital.getComplement()),
                hospital.getStatus()
        );
    }
}

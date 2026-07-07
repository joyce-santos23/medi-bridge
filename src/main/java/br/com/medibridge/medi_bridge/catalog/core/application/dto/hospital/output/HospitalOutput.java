package br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
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
    public static HospitalOutput from(Hospital hospital, AddressBase addressBase) {
        if (hospital == null) {
            return null;
        }
        return new HospitalOutput(
                hospital.getId(),
                hospital.getName(),
                hospital.getCnpj().getValue(),
                hospital.getCnes(),
                hospital.getEmail(),
                hospital.getPhone(),
                AddressOutput.from(addressBase, hospital.getNumber(), hospital.getComplement()),
                hospital.getStatus()
        );
    }
}

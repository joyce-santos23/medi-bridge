package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.AddressInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.AddressOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Address;

final class HospitalUseCaseMapper {

    private HospitalUseCaseMapper() {
    }

    static Address toAddress(AddressInput input) {
        if (input == null) {
            return null;
        }
        return Address.of(
                input.street(),
                input.number(),
                input.complement(),
                input.neighborhood(),
                input.city(),
                input.state(),
                input.zipCode()
        );
    }

    static HospitalOutput toOutput(Hospital hospital) {
        return new HospitalOutput(
                hospital.getId(),
                hospital.getName(),
                hospital.getCnpj().getValue(),
                hospital.getCnes(),
                hospital.getEmail(),
                hospital.getPhone(),
                toOutput(hospital.getAddress()),
                hospital.getStatus()
        );
    }

    private static AddressOutput toOutput(Address address) {
        return new AddressOutput(
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode()
        );
    }
}

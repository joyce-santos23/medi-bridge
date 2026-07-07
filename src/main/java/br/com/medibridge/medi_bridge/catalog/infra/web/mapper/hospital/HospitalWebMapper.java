package br.com.medibridge.medi_bridge.catalog.infra.web.mapper.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.AddressInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.RegisterHospitalInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.UpdateHospitalInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.AddressOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.RegisterHospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.infra.web.mapper.user.UserWebMapper;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.HospitalAddressResponse;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.HospitalResponse;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.RegisterHospitalRequest;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.RegisterHospitalResponse;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.UpdateHospitalRequest;

public class HospitalWebMapper {

    public static RegisterHospitalInput toInput(RegisterHospitalRequest request) {

        if (request == null) {
            return null;
        }

        AddressInput addressInput = null;

        if (request.address() != null) {
            addressInput = new AddressInput(
                    request.address().zipCode(),
                    request.address().number(),
                    request.address().complement()
            );
        }

        return new RegisterHospitalInput(
                request.name(),
                request.cnpj(),
                request.cnes(),
                request.email(),
                request.phone(),
                addressInput,
                request.adminName(),
                request.adminEmail(),
                ProfessionalCouncil.valueOf(request.adminCouncil().name()),
                request.adminProfessionalRegistration(),
                request.adminPassword()
        );
    }

    public static UpdateHospitalInput toInput(UpdateHospitalRequest request) {

        if (request == null) {
            return null;
        }

        return new UpdateHospitalInput(
                request.email(),
                request.phone(),
                HospitalStatus.valueOf(request.status().name())
        );
    }

    public static HospitalResponse toResponse(HospitalOutput output) {

        if (output == null) {
            return null;
        }

        HospitalAddressResponse addressResponse = null;

        if (output.address() != null) {
            AddressOutput address = output.address();

            addressResponse = new HospitalAddressResponse(
                    address.street(),
                    address.number(),
                    address.complement(),
                    address.neighborhood(),
                    address.city(),
                    address.state(),
                    address.zipCode()
            );
        }

        return new HospitalResponse(
                output.id(),
                output.name(),
                output.cnpj(),
                output.cnes(),
                output.email(),
                output.phone(),
                addressResponse,
                output.status()
        );
    }

    public static RegisterHospitalResponse toResponse(RegisterHospitalOutput output) {

        if (output == null) {
            return null;
        }

        return new RegisterHospitalResponse(
                toResponse(output.hospital()),
                UserWebMapper.toResponse(output.adminUser())
        );
    }
}
package br.com.medibridge.medi_bridge.catalog.infra.web.mapper.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.AddressInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.RegisterHospitalInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.UpdateHospitalInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.AddressOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.RegisterHospitalOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.infra.web.mapper.user.UserWebMapper;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.HospitalAddressResponsePayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.HospitalResponsePayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.RegisterHospitalRequestPayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.RegisterHospitalResponsePayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.UpdateHospitalRequestPayload;

public final class HospitalWebMapper {

    private HospitalWebMapper() {
    }

    public static RegisterHospitalInputDTO toInput(RegisterHospitalRequestPayload request) {
        if (request == null) {
            return null;
        }

        AddressInputDTO addressInput = null;

        if (request.address() != null) {
            addressInput = new AddressInputDTO(
                    request.address().zipCode(),
                    request.address().number(),
                    request.address().complement()
            );
        }

        return new RegisterHospitalInputDTO(
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

    public static UpdateHospitalInputDTO toInput(UpdateHospitalRequestPayload request) {
        if (request == null) {
            return null;
        }

        return new UpdateHospitalInputDTO(
                request.email(),
                request.phone(),
                request.status() != null ? HospitalStatus.valueOf(request.status().name()) : null
        );
    }

    public static HospitalResponsePayload toResponse(HospitalOutputDTO output) {
        if (output == null) {
            return null;
        }

        HospitalAddressResponsePayload addressResponse = null;

        if (output.address() != null) {
            AddressOutputDTO address = output.address();

            addressResponse = new HospitalAddressResponsePayload(
                    address.street(),
                    address.number(),
                    address.complement(),
                    address.neighborhood(),
                    address.city(),
                    address.state(),
                    address.zipCode()
            );
        }

        return new HospitalResponsePayload(
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


    public static RegisterHospitalResponsePayload toResponse(RegisterHospitalOutputDTO output) {
        if (output == null) {
            return null;
        }

        return new RegisterHospitalResponsePayload(
                toResponse(output.hospital()),
                UserWebMapper.toResponse(output.adminUser())
        );
    }
}
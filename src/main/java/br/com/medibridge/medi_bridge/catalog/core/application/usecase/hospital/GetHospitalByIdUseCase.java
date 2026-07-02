package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.entity.AddressBase;
import br.com.medibridge.medi_bridge.catalog.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import java.util.UUID;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetHospitalByIdUseCase {

    private final HospitalGateway hospitalGateway;
    private final AddressBaseGateway addressBaseGateway;

    public HospitalOutput execute(AuthenticatedUser currentUser, UUID id) {
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.role() != Role.ADMIN && !id.equals(currentUser.hospitalId())) {
            throw new ForbiddenException("Access denied to this hospital");
        }

        Hospital hospital = hospitalGateway.findById(id)
                .orElseThrow(() -> new NotFoundException("Hospital not found"));
        
        AddressBase addressBase = addressBaseGateway.findById(hospital.getAddressBaseId())
                .orElseThrow(() -> new NotFoundException("Address base not found"));

        return HospitalOutput.from(hospital, addressBase);
    }
}

package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import java.util.UUID;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetHospitalByIdUseCase {

    private final HospitalGateway hospitalGateway;
    private final AddressBaseGateway addressBaseGateway;

    public HospitalOutput execute(AuthenticatedUser currentUser, UUID id) {
        log.info("Executing GetHospitalByIdUseCase for hospital ID: {} by user ID: {}", id, currentUser != null ? currentUser.id() : "anonymous");
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.role() != Role.ADMIN && !id.equals(currentUser.hospitalId())) {
            log.warn("Access denied for user ID: {} attempting to get hospital ID: {}", currentUser.id(), id);
            throw new ForbiddenException("Access denied to this hospital");
        }

        Hospital hospital = hospitalGateway.findById(id)
                .orElseThrow(() -> {
                    log.warn("Hospital with ID: {} not found", id);
                    return new NotFoundException("Hospital not found");
                });
        
        AddressBase addressBase = addressBaseGateway.findById(hospital.getAddressBaseId())
                .orElseThrow(() -> {
                    log.error("Address base not found for hospital ID: {}", id);
                    return new NotFoundException("Address base not found");
                });

        log.info("Successfully retrieved hospital details for ID: {}", id);
        return HospitalOutput.from(hospital, addressBase);
    }
}

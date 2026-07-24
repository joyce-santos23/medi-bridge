package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.UpdateHospitalInputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutputDTO;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateHospitalUseCase {

    private final HospitalGateway hospitalGateway;
    private final AddressBaseGateway addressBaseGateway;

    public HospitalOutputDTO execute(AuthenticatedUser currentUser, UUID hospitalId, UpdateHospitalInputDTO input) {
        log.info("Executing UpdateHospitalUseCase for hospital ID: {} by user ID: {}", hospitalId, currentUser != null ? currentUser.id() : "anonymous");
        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        if (currentUser.role() != Role.ADMIN || !hospitalId.equals(currentUser.hospitalId())) {
            log.warn("Access denied for user ID: {} attempting to update hospital ID: {}", currentUser.id(), hospitalId);
            throw new ForbiddenException("Only administrators of this hospital can update it");
        }

        Hospital hospital = hospitalGateway.findById(hospitalId)
                .orElseThrow(() -> {
                    log.warn("Hospital with ID: {} not found", hospitalId);
                    return new NotFoundException("Hospital not found");
                });

        hospital.update(
                input.email(),
                input.phone(),
                input.status()
        );

        Hospital updatedHospital = hospitalGateway.save(hospital);
        log.info("Hospital entity with ID: {} updated successfully", hospitalId);

        AddressBase addressBase = addressBaseGateway.findById(updatedHospital.getAddressBaseId())
                .orElseThrow(() -> {
                    log.error("Address base not found for hospital ID: {}", hospitalId);
                    return new NotFoundException("Address base not found");
                });

        return HospitalOutputDTO.from(updatedHospital, addressBase);
    }
}

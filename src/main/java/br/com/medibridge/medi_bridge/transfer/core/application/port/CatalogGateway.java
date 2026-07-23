package br.com.medibridge.medi_bridge.transfer.core.application.port;

import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.UserSummary;

import java.util.Optional;
import java.util.UUID;

public interface CatalogGateway {
    Optional<HospitalSummary> findHospitalById(UUID hospitalId);
    Optional<UserSummary> findUserById(UUID userId);
}

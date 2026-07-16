package br.com.medibridge.medi_bridge.transfer.core.application.port;

import java.util.Optional;
import java.util.UUID;

public interface CatalogGateway {
    Optional<HospitalSummary> findHospitalById(UUID hospitalId);
    Optional<UserSummary> findUserById(UUID userId);

    record HospitalSummary(UUID id, String name, boolean active) {}
    record UserSummary(UUID id, String name, boolean active, UUID hospitalId) {}
}

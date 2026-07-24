package br.com.medibridge.medi_bridge.notification.infra.integration;

import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.notification.core.application.dto.integration.NotificationHospitalSummary;
import br.com.medibridge.medi_bridge.notification.core.application.port.NotificationCatalogGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CatalogIntegrationAdapter implements NotificationCatalogGateway {

    private final HospitalGateway hospitalGateway;

    @Override
    public Optional<NotificationHospitalSummary> findHospitalById(UUID id) {
        return hospitalGateway.findById(id)
                .map(hospital -> new NotificationHospitalSummary(
                        hospital.getId(),
                        hospital.getName(),
                        hospital.getEmail()
                ));
    }

    @Override
    public List<NotificationHospitalSummary> findAllActiveHospitals() {
        return hospitalGateway.findAllActive().stream()
                .map(hospital -> new NotificationHospitalSummary(
                        hospital.getId(),
                        hospital.getName(),
                        hospital.getEmail()
                ))
                .toList();
    }
}

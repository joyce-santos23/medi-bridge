package br.com.medibridge.medi_bridge.offer.infra.integration;

import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.offer.core.application.port.CatalogGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CatalogAdapter implements CatalogGateway {

    private final HospitalGateway hospitalGateway;

    @Override
    public Optional<HospitalSummary> findHospitalById(UUID hospitalId) {
        return hospitalGateway.findById(hospitalId)
                .map(hospital -> new HospitalSummary(
                        hospital.getId(),
                        hospital.getName(),
                        hospital.isActive()
                ));
    }
}

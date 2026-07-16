package br.com.medibridge.medi_bridge.transfer.infra.integration;

import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import br.com.medibridge.medi_bridge.transfer.core.application.port.CatalogGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("transferCatalogAdapter")
@RequiredArgsConstructor
public class CatalogAdapter implements CatalogGateway {

    private final HospitalGateway hospitalGateway;
    private final UserGateway userGateway;

    @Override
    public Optional<HospitalSummary> findHospitalById(UUID hospitalId) {
        return hospitalGateway.findById(hospitalId)
                .map(hospital -> new HospitalSummary(
                        hospital.getId(),
                        hospital.getName(),
                        hospital.isActive()
                ));
    }

    @Override
    public Optional<UserSummary> findUserById(UUID userId) {
        return userGateway.findById(userId)
                .map(user -> new UserSummary(
                        user.getId(),
                        user.getName(),
                        user.getStatus() == UserStatus.ACTIVE,
                        user.getHospitalId()
                ));
    }
}

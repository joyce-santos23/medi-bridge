package br.com.medibridge.medi_bridge.transfer.infra.integration;

import br.com.medibridge.medi_bridge.catalog.core.application.port.address.AddressBaseGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.AddressSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.HospitalSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.integration.UserSummary;
import br.com.medibridge.medi_bridge.transfer.core.application.port.CatalogGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("transferCatalogAdapter")
@RequiredArgsConstructor
public class CatalogAdapter implements CatalogGateway {

    private final HospitalGateway hospitalGateway;
    private final AddressBaseGateway addressBaseGateway;
    private final UserGateway userGateway;

    @Override
    public Optional<HospitalSummary> findHospitalById(UUID hospitalId) {
        return hospitalGateway.findById(hospitalId)
                .map(hospital -> {
                    AddressBase address = addressBaseGateway.findById(hospital.getAddressBaseId()).orElse(null);
                    AddressSummary addressSummary = address != null ? new AddressSummary(
                            address.getZipCode(),
                            address.getStreet(),
                            address.getNeighborhood(),
                            address.getCity(),
                            address.getState(),
                            hospital.getNumber(),
                            hospital.getComplement()
                    ) : null;

                    return new HospitalSummary(
                            hospital.getId(),
                            hospital.getName(),
                            hospital.isActive(),
                            hospital.getEmail(),
                            hospital.getPhone(),
                            addressSummary
                    );
                });
    }

    @Override
    public Optional<UserSummary> findUserById(UUID userId) {
        return userGateway.findById(userId)
                .map(user -> new UserSummary(
                        user.getId(),
                        user.getName(),
                        user.getCouncil() != null ? user.getCouncil().name() : null,
                        user.getProfessionalRegistration()
                ));
    }
}

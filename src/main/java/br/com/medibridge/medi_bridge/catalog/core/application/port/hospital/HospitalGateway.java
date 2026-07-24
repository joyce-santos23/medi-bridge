package br.com.medibridge.medi_bridge.catalog.core.application.port.hospital;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HospitalGateway {

    Hospital save(Hospital hospital);

    Optional<Hospital> findById(UUID id);

    boolean existsByCnpj(Cnpj cnpj);

    boolean existsByCnpjAndIdNot(Cnpj cnpj, UUID id);

    boolean existsByCnes(String cnes);

    boolean existsByCnesAndIdNot(String cnes, UUID id);

    List<Hospital> findAllActive();
}

package br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.hospital;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, UUID> {
    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndIdNot(String cnpj, UUID id);
    boolean existsByCnes(String cnes);
    boolean existsByCnesAndIdNot(String cnes, UUID id);
    List<HospitalEntity> findByStatus(HospitalStatus status);
}

package br.com.medibridge.medi_bridge.transfer.core.application.port;

import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransferGateway {
    Transfer save(Transfer transfer);
    Optional<Transfer> findById(UUID id);
    List<Transfer> findAll();
    List<Transfer> findActiveTransfersBefore(Instant time);
    List<Transfer> findByHospital(UUID hospitalId);
    List<Transfer> findByOfferId(UUID offerId);
    List<Transfer> findPendingApprovalsBySourceHospital(UUID sourceHospitalId);
}

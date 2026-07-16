package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.shared.domain.exception.ForbiddenException;
import br.com.medibridge.medi_bridge.transfer.core.application.dto.response.TransferResponseDTO;
import br.com.medibridge.medi_bridge.transfer.core.application.port.TransferGateway;
import br.com.medibridge.medi_bridge.transfer.core.domain.exception.TransferNotFoundException;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTransferUseCase {

    private final TransferGateway transferGateway;

    public TransferResponseDTO execute(AuthenticatedUser currentUser, UUID transferId) {
        log.info("Executing GetTransferUseCase for transfer ID: {} by user: {}", transferId,
                currentUser != null ? currentUser.id() : "anonymous");

        if (currentUser == null) {
            throw new ForbiddenException("Authentication required");
        }

        Transfer transfer = transferGateway.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException("Transfer request not found"));

        // Apenas o hospital de origem ou de destino pode visualizar os detalhes da transferência
        UUID userHospitalId = currentUser.hospitalId();
        if (userHospitalId == null || 
            (!userHospitalId.equals(transfer.getSourceHospitalId())
                    && !userHospitalId.equals(transfer.getDestinationHospitalId()))) {
            throw new ForbiddenException("Access denied to this transfer request details");
        }

        boolean isOwner = userHospitalId.equals(transfer.getSourceHospitalId());
        return TransferResponseDTO.from(transfer, isOwner);
    }
}

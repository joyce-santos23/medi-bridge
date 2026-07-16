package br.com.medibridge.medi_bridge.transfer.core.application.usecase;

import br.com.medibridge.medi_bridge.transfer.core.application.port.DomainEventPublisherGateway;
import br.com.medibridge.medi_bridge.transfer.core.application.port.OfferGateway;
import br.com.medibridge.medi_bridge.transfer.core.application.port.TransferGateway;
import br.com.medibridge.medi_bridge.transfer.core.domain.entity.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpireTransfersUseCase {

    private final TransferGateway transferGateway;
    private final OfferGateway offerGateway;
    private final DomainEventPublisherGateway domainEventPublisherGateway;

    public int execute() {
        log.info("Executing ExpireTransfersUseCase to process expired transfer requests");

        Instant now = Instant.now();
        List<Transfer> expiredTransfers = transferGateway.findActiveTransfersBefore(now);

        log.info("Found {} transfer requests to expire", expiredTransfers.size());

        int count = 0;
        for (Transfer transfer : expiredTransfers) {
            try {
                transfer.expire(now);

                offerGateway.release(transfer.getOfferId());
                transferGateway.save(transfer);

                domainEventPublisherGateway.publish(transfer.pullDomainEvents());

                count++;
            } catch (Exception e) {
                log.error("Failed to expire transfer request with ID: {}", transfer.getId(), e);
            }
        }

        log.info("Successfully expired {} transfer requests", count);
        return count;
    }
}

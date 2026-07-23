package br.com.medibridge.medi_bridge.transfer.infra.scheduler;

import br.com.medibridge.medi_bridge.transfer.core.application.usecase.ExpireTransfersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferExpirationScheduler {

    private final ExpireTransfersUseCase expireTransfersUseCase;

    // Runs every hour to check and expire outdated transfer requests
    @Scheduled(cron = "0 0 * * * *")
    public void expireOutdatedTransfers() {
        log.info("Starting scheduled task to expire outdated transfer requests");
        try {
            int expiredCount = expireTransfersUseCase.execute();
            log.info("Finished scheduled task. Expired {} transfers.", expiredCount);
        } catch (Exception e) {
            log.error("Failed to run scheduled transfer expiration task", e);
        }
    }
}

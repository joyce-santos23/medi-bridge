package br.com.medibridge.medi_bridge.offer.infra.scheduler;

import br.com.medibridge.medi_bridge.offer.core.application.port.OfferRepositoryGateway;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.ExpireOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfferExpirationScheduler {

    private final OfferRepositoryGateway offerRepositoryGateway;
    private final ExpireOfferUseCase expireOfferUseCase;

    // Runs every day at 1 AM
    @Scheduled(cron = "0 0 1 * * *")
    public void expireOutdatedOffers() {
        log.info("Starting scheduled task to expire offers with validity below minimum threshold");

        LocalDate limitDate = LocalDate.now().plusDays(90);

        List<Offer> offersToExpire = offerRepositoryGateway.findAllAvailable().stream()
                .filter(offer -> offer.getProduct().getExpirationDate().isBefore(limitDate))
                .toList();

        log.info("Found {} offers to expire", offersToExpire.size());

        for (Offer offer : offersToExpire) {
            try {
                expireOfferUseCase.execute(offer.getId());
            } catch (Exception e) {
                log.error("Failed to expire offer with ID: {}", offer.getId(), e);
            }
        }

        log.info("Finished scheduled task to expire offers");
    }
}

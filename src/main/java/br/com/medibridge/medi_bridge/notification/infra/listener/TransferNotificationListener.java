package br.com.medibridge.medi_bridge.notification.infra.listener;

import br.com.medibridge.medi_bridge.transfer.core.domain.event.*;
import br.com.medibridge.medi_bridge.notification.core.application.usecase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferNotificationListener {

    private final SendTransferCreatedNotificationUseCase sendTransferCreatedNotificationUseCase;
    private final SendTransferApprovedNotificationUseCase sendTransferApprovedNotificationUseCase;
    private final SendTransferRejectedNotificationUseCase sendTransferRejectedNotificationUseCase;
    private final SendTransferCompletedNotificationUseCase sendTransferCompletedNotificationUseCase;
    private final SendTransferExpiredNotificationUseCase sendTransferExpiredNotificationUseCase;

    @EventListener
    @Async
    public void onTransferCreated(TransferCreated event) {
        log.info("Received TransferCreated event for transfer ID: {}", event.transferId());
        try {
            sendTransferCreatedNotificationUseCase.execute(event.transferId());
        } catch (Exception e) {
            log.error("Error processing TransferCreated notification for transfer ID: {}", event.transferId(), e);
        }
    }

    @EventListener
    @Async
    public void onTransferApproved(TransferApproved event) {
        log.info("Received TransferApproved event for transfer ID: {}", event.transferId());
        try {
            sendTransferApprovedNotificationUseCase.execute(event.transferId());
        } catch (Exception e) {
            log.error("Error processing TransferApproved notification for transfer ID: {}", event.transferId(), e);
        }
    }

    @EventListener
    @Async
    public void onTransferRejected(TransferRejected event) {
        log.info("Received TransferRejected event for transfer ID: {}", event.transferId());
        try {
            sendTransferRejectedNotificationUseCase.execute(event.transferId());
        } catch (Exception e) {
            log.error("Error processing TransferRejected notification for transfer ID: {}", event.transferId(), e);
        }
    }

    @EventListener
    @Async
    public void onTransferCompleted(TransferCompleted event) {
        log.info("Received TransferCompleted event for transfer ID: {}", event.transferId());
        try {
            sendTransferCompletedNotificationUseCase.execute(event.transferId());
        } catch (Exception e) {
            log.error("Error processing TransferCompleted notification for transfer ID: {}", event.transferId(), e);
        }
    }

    @EventListener
    @Async
    public void onTransferExpired(TransferExpired event) {
        log.info("Received TransferExpired event for transfer ID: {}", event.transferId());
        try {
            sendTransferExpiredNotificationUseCase.execute(event.transferId());
        } catch (Exception e) {
            log.error("Error processing TransferExpired notification for transfer ID: {}", event.transferId(), e);
        }
    }
}

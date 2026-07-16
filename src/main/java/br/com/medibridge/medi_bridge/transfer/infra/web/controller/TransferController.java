package br.com.medibridge.medi_bridge.transfer.infra.web.controller;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.transfer.core.application.usecase.*;
import br.com.medibridge.medi_bridge.transfer.infra.web.mapper.TransferWebMapper;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.request.*;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.response.TransferResponsePayload;
import br.com.medibridge.medi_bridge.transfer.infra.web.payload.response.TransferSummaryResponsePayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final CreateTransferUseCase createTransferUseCase;
    private final ListTransfersUseCase listTransfersUseCase;
    private final GetTransferUseCase getTransferUseCase;
    private final ApproveTransferUseCase approveTransferUseCase;
    private final RejectTransferUseCase rejectTransferUseCase;
    private final CancelTransferUseCase cancelTransferUseCase;
    private final CompleteTransferUseCase completeTransferUseCase;
    private final ExpireTransfersUseCase expireTransfersUseCase;

    @PostMapping
    public ResponseEntity<TransferResponsePayload> create(
            @Valid @RequestBody CreateTransferRequestPayload payload,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to create transfer by user: {}", currentUser != null ? currentUser.id() : "anonymous");
        var dto = TransferWebMapper.toDTO(payload);
        var result = createTransferUseCase.execute(currentUser, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransferWebMapper.toResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<TransferSummaryResponsePayload>> list(
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to list transfers for hospital");
        var results = listTransfersUseCase.execute(currentUser);
        var response = results.stream()
                .map(TransferWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponsePayload> getById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to retrieve transfer details for ID: {}", id);
        var result = getTransferUseCase.execute(currentUser, id);
        return ResponseEntity.ok(TransferWebMapper.toResponse(result));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TransferResponsePayload> approve(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to approve transfer ID: {}", id);
        var result = approveTransferUseCase.execute(currentUser, id);
        return ResponseEntity.ok(TransferWebMapper.toResponse(result));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<TransferResponsePayload> reject(
            @PathVariable("id") UUID id,
            @Valid @RequestBody RejectTransferRequestPayload payload,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to reject transfer ID: {}", id);
        var dto = TransferWebMapper.toDTO(payload);
        var result = rejectTransferUseCase.execute(currentUser, id, dto);
        return ResponseEntity.ok(TransferWebMapper.toResponse(result));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<TransferResponsePayload> cancel(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CancelTransferRequestPayload payload,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to cancel transfer ID: {}", id);
        var dto = TransferWebMapper.toDTO(payload);
        var result = cancelTransferUseCase.execute(currentUser, id, dto);
        return ResponseEntity.ok(TransferWebMapper.toResponse(result));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<TransferResponsePayload> complete(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CompleteTransferRequestPayload payload,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to complete transfer ID: {}", id);
        var dto = TransferWebMapper.toDTO(payload);
        var result = completeTransferUseCase.execute(currentUser, id, dto);
        return ResponseEntity.ok(TransferWebMapper.toResponse(result));
    }

    @PostMapping("/expire")
    public ResponseEntity<Integer> expire() {
        log.info("Request to execute transfers expiration process");
        int count = expireTransfersUseCase.execute();
        return ResponseEntity.ok(count);
    }
}

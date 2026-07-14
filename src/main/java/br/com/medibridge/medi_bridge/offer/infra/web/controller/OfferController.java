package br.com.medibridge.medi_bridge.offer.infra.web.controller;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.PublishOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.UpdateOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.CancelOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.GetOfferUseCase;
import br.com.medibridge.medi_bridge.offer.core.application.usecase.offer.ListAvailableOffersUseCase;
import br.com.medibridge.medi_bridge.offer.infra.web.mapper.OfferWebMapper;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.request.PublishOfferRequestPayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.request.UpdateOfferRequestPayload;
import br.com.medibridge.medi_bridge.offer.infra.web.payload.response.OfferResponsePayload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/offers")
@RequiredArgsConstructor
@Slf4j
public class OfferController {

    private final PublishOfferUseCase publishOfferUseCase;
    private final UpdateOfferUseCase updateOfferUseCase;
    private final CancelOfferUseCase cancelOfferUseCase;
    private final GetOfferUseCase getOfferUseCase;
    private final ListAvailableOffersUseCase listAvailableOffersUseCase;

    @PostMapping
    public ResponseEntity<OfferResponsePayload> publish(
            @Valid @RequestBody PublishOfferRequestPayload request,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to publish offer by user: {}", currentUser != null ? currentUser.id() : "anonymous");
        var input = OfferWebMapper.toDTO(request);
        var output = publishOfferUseCase.execute(currentUser, input);
        var response = OfferWebMapper.toResponse(output);
        log.info("Offer successfully published with ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferResponsePayload> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateOfferRequestPayload request,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to update offer with ID: {}", id);
        var input = OfferWebMapper.toDTO(id, request);
        var output = updateOfferUseCase.execute(currentUser, input);
        var response = OfferWebMapper.toResponse(output);
        log.info("Offer successfully updated with ID: {}", response.id());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OfferResponsePayload> cancel(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to cancel offer with ID: {}", id);
        var output = cancelOfferUseCase.execute(currentUser, id);
        var response = OfferWebMapper.toResponse(output);
        log.info("Offer successfully cancelled with ID: {}", response.id());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponsePayload> getById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to retrieve offer details for ID: {}", id);
        var output = getOfferUseCase.execute(currentUser, id);
        var response = OfferWebMapper.toResponse(output);
        log.info("Offer details retrieved successfully for ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<OfferResponsePayload>> listAvailable(
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to list all available offers");
        var outputs = listAvailableOffersUseCase.execute(currentUser);
        var response = outputs.stream()
                .map(OfferWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}

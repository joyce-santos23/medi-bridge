package br.com.medibridge.medi_bridge.transfer.infra.web.payload.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateTransferRequestPayload(
        @NotNull(message = "Offer ID is required")
        UUID offerId,
        String reason
) {}

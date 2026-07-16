package br.com.medibridge.medi_bridge.transfer.infra.web.payload.request;

import jakarta.validation.constraints.NotBlank;

public record CompleteTransferRequestPayload(
        @NotBlank(message = "Confirmation code is required")
        String confirmationCode
) {}

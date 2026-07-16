package br.com.medibridge.medi_bridge.transfer.infra.web.payload.request;

import jakarta.validation.constraints.NotBlank;

public record RejectTransferRequestPayload(
        @NotBlank(message = "Reason is required to reject")
        String reason
) {}

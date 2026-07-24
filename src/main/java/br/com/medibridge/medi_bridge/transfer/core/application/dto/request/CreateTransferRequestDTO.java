package br.com.medibridge.medi_bridge.transfer.core.application.dto.request;

import java.util.UUID;

public record CreateTransferRequestDTO(
        UUID offerId,
        String reason
) {}

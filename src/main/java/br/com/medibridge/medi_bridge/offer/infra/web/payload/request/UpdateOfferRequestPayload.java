package br.com.medibridge.medi_bridge.offer.infra.web.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateOfferRequestPayload(
        @NotNull
        @Valid
        ProductRequestPayload product
) {
}

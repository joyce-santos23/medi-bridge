package br.com.medibridge.medi_bridge.offer.infra.web.payload.request;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Category;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ProductRequestPayload(
        @NotBlank
        String name,

        @NotNull
        Category category,

        String manufacturer,

        @NotBlank
        String batch,

        @NotNull
        LocalDate expirationDate,

        @NotNull
        @Positive
        Integer quantity,

        @NotNull
        Unit unit,

        String observations
) {
}

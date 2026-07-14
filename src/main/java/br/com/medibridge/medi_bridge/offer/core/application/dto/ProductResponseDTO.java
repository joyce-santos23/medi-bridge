package br.com.medibridge.medi_bridge.offer.core.application.dto;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Category;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.enums.Unit;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.valueobject.Product;
import java.time.LocalDate;

public record ProductResponseDTO(
        String name,
        Category category,
        String manufacturer,
        String batch,
        LocalDate expirationDate,
        Integer quantity,
        Unit unit,
        String observations
) {
    public static ProductResponseDTO from(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponseDTO(
                product.getName(),
                product.getCategory(),
                product.getManufacturer(),
                product.getBatch(),
                product.getExpirationDate(),
                product.getQuantity(),
                product.getUnit(),
                product.getObservations()
        );
    }
}

package br.com.medibridge.medi_bridge.offer.infra.persistence.mapper;

import br.com.medibridge.medi_bridge.offer.core.domain.offer.entity.Offer;
import br.com.medibridge.medi_bridge.offer.core.domain.offer.valueobject.Product;
import br.com.medibridge.medi_bridge.offer.infra.persistence.entity.OfferEntity;

public final class OfferPersistenceMapper {

    private OfferPersistenceMapper() {
    }

    public static OfferEntity toEntity(Offer domain) {
        OfferEntity entity = new OfferEntity();
        entity.setId(domain.getId());
        entity.setHospitalId(domain.getHospitalId());
        entity.setCreatedByUserId(domain.getCreatedByUserId());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        Product product = domain.getProduct();
        entity.setProductName(product.getName());
        entity.setProductCategory(product.getCategory());
        entity.setProductManufacturer(product.getManufacturer());
        entity.setProductBatch(product.getBatch());
        entity.setProductExpirationDate(product.getExpirationDate());
        entity.setProductQuantity(product.getQuantity());
        entity.setProductUnit(product.getUnit());
        entity.setProductObservations(product.getObservations());

        return entity;
    }

    public static Offer toDomain(OfferEntity entity) {
        Product product = new Product(
                entity.getProductName(),
                entity.getProductCategory(),
                entity.getProductManufacturer(),
                entity.getProductBatch(),
                entity.getProductExpirationDate(),
                entity.getProductQuantity(),
                entity.getProductUnit(),
                entity.getProductObservations()
        );

        return Offer.restore(
                entity.getId(),
                entity.getHospitalId(),
                entity.getCreatedByUserId(),
                product,
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

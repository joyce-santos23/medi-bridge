package br.com.medibridge.medi_bridge.offer.core.application.port;

import java.util.UUID;

public interface CatalogGateway {
    boolean existsHospitalById(UUID hospitalId);
    boolean existsUserById(UUID userId);
}

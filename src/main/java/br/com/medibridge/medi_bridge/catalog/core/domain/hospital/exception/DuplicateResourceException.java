package br.com.medibridge.medi_bridge.catalog.core.domain.hospital.exception;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.DomainException;

public class DuplicateResourceException extends DomainException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}

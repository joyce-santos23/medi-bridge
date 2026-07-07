package br.com.medibridge.medi_bridge.catalog.core.domain.exception;

import br.com.medibridge.medi_bridge.catalog.core.domain.exception.DomainException;

public class DuplicateResourceException extends DomainException {

    private final String fieldName;

    public DuplicateResourceException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

package br.com.medibridge.medi_bridge.shared.domain.exception;

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

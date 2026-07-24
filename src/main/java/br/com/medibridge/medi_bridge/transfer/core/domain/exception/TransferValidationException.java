package br.com.medibridge.medi_bridge.transfer.core.domain.exception;

import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;

public class TransferValidationException extends ValidationException {
    public TransferValidationException(String message) {
        super(message);
    }
}

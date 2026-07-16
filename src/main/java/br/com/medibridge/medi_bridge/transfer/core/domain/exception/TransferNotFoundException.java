package br.com.medibridge.medi_bridge.transfer.core.domain.exception;

import br.com.medibridge.medi_bridge.shared.domain.exception.NotFoundException;

public class TransferNotFoundException extends NotFoundException {
    public TransferNotFoundException(String message) {
        super(message);
    }
}

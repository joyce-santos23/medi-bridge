package br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.mapper;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutputDTO;
import br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.payload.ViaCepResponsePayload;
import org.springframework.stereotype.Component;

@Component
public class ViaCepMapper {

    public ViaCepAddressOutputDTO toOutput(ViaCepResponsePayload response) {
        if (response == null) {
            return null;
        }

        String normalizedZipCode = response.cep() != null 
                ? response.cep().replaceAll("\\D", "") 
                : null;

        return new ViaCepAddressOutputDTO(
                normalizedZipCode,
                response.logradouro(),
                response.bairro(),
                response.localidade(),
                response.uf()
        );
    }
}

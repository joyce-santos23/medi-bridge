package br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.mapper;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutput;
import br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.payload.ViaCepResponse;
import org.springframework.stereotype.Component;

@Component
public class ViaCepMapper {

    public ViaCepAddressOutput toOutput(ViaCepResponse response) {
        if (response == null) {
            return null;
        }

        String normalizedZipCode = response.cep() != null 
                ? response.cep().replaceAll("\\D", "") 
                : null;

        return new ViaCepAddressOutput(
                normalizedZipCode,
                response.logradouro(),
                response.bairro(),
                response.localidade(),
                response.uf()
        );
    }
}

package br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.adapter;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.address.output.ViaCepAddressOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.address.ViaCepGateway;
import br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.mapper.ViaCepMapper;
import br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.payload.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ViaCepAdapter implements ViaCepGateway {

    private final RestClient viaCepRestClient;
    private final ViaCepMapper viaCepMapper;

    @Override
    public Optional<ViaCepAddressOutput> findByZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            return Optional.empty();
        }

        try {
            ViaCepResponse response = viaCepRestClient.get()
                    .uri("/ws/{cep}/json", zipCode)
                    .retrieve()
                    .body(ViaCepResponse.class);

            if (response == null || Boolean.TRUE.equals(response.erro())) {
                return Optional.empty();
            }

            return Optional.ofNullable(viaCepMapper.toOutput(response));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

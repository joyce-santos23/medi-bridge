package br.com.medibridge.medi_bridge.catalog.infra.integration.viacep.payload;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {
}

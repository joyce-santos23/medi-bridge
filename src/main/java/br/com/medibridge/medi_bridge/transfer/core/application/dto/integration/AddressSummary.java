package br.com.medibridge.medi_bridge.transfer.core.application.dto.integration;

public record AddressSummary(
        String zipCode,
        String street,
        String neighborhood,
        String city,
        String state,
        String number,
        String complement
) {}

package br.com.medibridge.medi_bridge.notification.core.application.dto;

public record EmailRequestDTO(
        String to,
        String subject,
        String body
) {}

package br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital;

import br.com.medibridge.medi_bridge.catalog.infra.web.payload.enums.ProfessionalCouncil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record RegisterHospitalRequest(
        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @CNPJ
        String cnpj,

        @NotBlank
        @Size(max = 20)
        String cnes,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(max = 20)
        String phone,

        @NotNull
        @Valid
        RegisterHospitalAddressRequest address,

        @NotBlank
        @Size(max = 100)
        String adminName,

        @NotBlank
        @Email
        @Size(max = 100)
        String adminEmail,

        @NotNull
        ProfessionalCouncil adminCouncil,

        @NotBlank
        @Size(max = 50)
        String adminProfessionalRegistration,

        @NotBlank
        @Size(min = 6, max = 100)
        String adminPassword
) {
}

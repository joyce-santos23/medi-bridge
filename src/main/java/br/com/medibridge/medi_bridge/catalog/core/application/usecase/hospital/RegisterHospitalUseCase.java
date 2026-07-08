package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.usecase.address.FindOrCreateAddressBaseUseCase;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.RegisterHospitalInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.RegisterHospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.user.output.UserOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalGateway;
import br.com.medibridge.medi_bridge.auth.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.address.AddressBase;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.shared.domain.enums.Role;
import br.com.medibridge.medi_bridge.shared.domain.exception.DuplicateResourceException;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterHospitalUseCase {

    private final HospitalGateway hospitalGateway;
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final FindOrCreateAddressBaseUseCase findOrCreateAddressBaseUseCase;

    @Transactional
    public RegisterHospitalOutput execute(RegisterHospitalInput input) {
        log.info("Executing RegisterHospitalUseCase for hospital name: {}", input.name());
        Cnpj cnpj = Cnpj.of(input.cnpj());
        validateUniqueHospital(cnpj, input.cnes());

        AddressBase addressBase = findOrCreateAddressBaseUseCase.execute(input.address().zipCode());

        Hospital hospital = Hospital.create(
                input.name(),
                cnpj,
                input.cnes(),
                input.email(),
                input.phone(),
                addressBase.getId(),
                input.address().number(),
                input.address().complement()
        );

        Hospital savedHospital = hospitalGateway.save(hospital);
        log.info("Hospital entity saved with ID: {}", savedHospital.getId());

        User adminUser = User.create(
                savedHospital.getId(),
                input.adminName(),
                input.adminEmail(),
                input.adminCouncil(),
                input.adminProfessionalRegistration(),
                Role.ADMIN,
                passwordEncoder.encode(input.adminPassword())
        );

        User savedAdminUser = userGateway.save(adminUser);
        log.info("Hospital admin user saved with ID: {} for hospital ID: {}", savedAdminUser.getId(), savedHospital.getId());

        return new RegisterHospitalOutput(
                HospitalOutput.from(savedHospital, addressBase),
                UserOutput.from(savedAdminUser)
        );
    }

    private void validateUniqueHospital(Cnpj cnpj, String cnes) {
        log.debug("Validating uniqueness of CNPJ: {} and CNES: {}", cnpj.getValue(), cnes);
        if (hospitalGateway.existsByCnpj(cnpj)) {
            log.warn("Duplicate hospital registration attempt: CNPJ {} is already registered", cnpj.getValue());
            throw new DuplicateResourceException("cnpj", "CNPJ already registered");
        }
        if (hospitalGateway.existsByCnes(cnes)) {
            log.warn("Duplicate hospital registration attempt: CNES {} is already registered", cnes);
            throw new DuplicateResourceException("cnes", "CNES already registered");
        }
    }
}

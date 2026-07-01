package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.usecase.user.UserUseCaseMapper;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.RegisterHospitalInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.RegisterHospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalRepository;
import br.com.medibridge.medi_bridge.catalog.core.application.port.security.PasswordEncoder;
import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserRepository;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.exception.DuplicateResourceException;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterHospitalUseCase {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterHospitalOutput execute(RegisterHospitalInput input) {
        Cnpj cnpj = Cnpj.of(input.cnpj());
        validateUniqueHospital(cnpj, input.cnes());

        Hospital hospital = Hospital.create(
                input.name(),
                cnpj,
                input.cnes(),
                input.email(),
                input.phone(),
                HospitalUseCaseMapper.toAddress(input.address())
        );

        Hospital savedHospital = hospitalRepository.save(hospital);
        User adminUser = User.create(
                savedHospital.getId(),
                input.adminName(),
                input.adminEmail(),
                input.adminCouncil(),
                input.adminProfessionalRegistration(),
                Role.ADMIN,
                passwordEncoder.encode(input.adminPassword())
        );

        User savedAdminUser = userRepository.save(adminUser);

        return new RegisterHospitalOutput(
                HospitalUseCaseMapper.toOutput(savedHospital),
                UserUseCaseMapper.toOutput(savedAdminUser)
        );
    }

    private void validateUniqueHospital(Cnpj cnpj, String cnes) {
        if (hospitalRepository.existsByCnpj(cnpj)) {
            throw new DuplicateResourceException("CNPJ already registered");
        }
        if (hospitalRepository.existsByCnes(cnes)) {
            throw new DuplicateResourceException("CNES already registered");
        }
    }
}

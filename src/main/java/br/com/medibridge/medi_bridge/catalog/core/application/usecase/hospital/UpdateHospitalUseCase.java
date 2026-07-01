package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.input.UpdateHospitalInput;
import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalRepository;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity.Hospital;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.exception.DuplicateResourceException;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateHospitalUseCase {

    private final HospitalRepository hospitalRepository;

    public HospitalOutput execute(UpdateHospitalInput input) {
        Hospital hospital = hospitalRepository.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Hospital not found"));

        Cnpj cnpj = Cnpj.of(input.cnpj());
        validateUniqueHospital(input, cnpj);

        hospital.update(
                input.name(),
                cnpj,
                input.cnes(),
                input.email(),
                input.phone(),
                HospitalUseCaseMapper.toAddress(input.address()),
                input.status()
        );

        return HospitalUseCaseMapper.toOutput(hospitalRepository.save(hospital));
    }

    private void validateUniqueHospital(UpdateHospitalInput input, Cnpj cnpj) {
        if (hospitalRepository.existsByCnpjAndIdNot(cnpj, input.id())) {
            throw new DuplicateResourceException("CNPJ already registered");
        }
        if (hospitalRepository.existsByCnesAndIdNot(input.cnes(), input.id())) {
            throw new DuplicateResourceException("CNES already registered");
        }
    }
}

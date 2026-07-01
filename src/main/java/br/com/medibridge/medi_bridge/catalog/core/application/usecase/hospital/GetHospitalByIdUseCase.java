package br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital;

import br.com.medibridge.medi_bridge.catalog.core.application.dto.hospital.output.HospitalOutput;
import br.com.medibridge.medi_bridge.catalog.core.application.port.hospital.HospitalRepository;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetHospitalByIdUseCase {

    private final HospitalRepository hospitalRepository;

    public HospitalOutput execute(UUID id) {
        return hospitalRepository.findById(id)
                .map(HospitalUseCaseMapper::toOutput)
                .orElseThrow(() -> new NotFoundException("Hospital not found"));
    }
}

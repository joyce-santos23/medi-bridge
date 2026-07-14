package br.com.medibridge.medi_bridge.catalog.infra.web.controller.hospital;

import br.com.medibridge.medi_bridge.shared.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital.GetHospitalByIdUseCase;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital.RegisterHospitalUseCase;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.hospital.UpdateHospitalUseCase;
import br.com.medibridge.medi_bridge.catalog.infra.web.mapper.hospital.HospitalWebMapper;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.HospitalResponsePayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.RegisterHospitalRequestPayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.RegisterHospitalResponsePayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.hospital.UpdateHospitalRequestPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/hospitals")
@RequiredArgsConstructor
@Slf4j
public class HospitalController {

    private final RegisterHospitalUseCase registerHospitalUseCase;
    private final UpdateHospitalUseCase updateHospitalUseCase;
    private final GetHospitalByIdUseCase getHospitalByIdUseCase;

    @PostMapping
    public ResponseEntity<RegisterHospitalResponsePayload> register(
            @Valid @RequestBody RegisterHospitalRequestPayload request
    ) {
        log.info("Request to register hospital with CNPJ: {}", request.cnpj());
        var input = HospitalWebMapper.toInput(request);
        var output = registerHospitalUseCase.execute(input);
        var response = HospitalWebMapper.toResponse(output);
        log.info("Hospital successfully registered with ID: {}", response.hospital().id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponsePayload> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateHospitalRequestPayload request,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to update hospital ID: {}", id);
        var input = HospitalWebMapper.toInput(request);
        var output = updateHospitalUseCase.execute(currentUser, id, input);
        var response = HospitalWebMapper.toResponse(output);
        log.info("Hospital ID: {} successfully updated", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponsePayload> getById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to get hospital details for ID: {}", id);
        var output = getHospitalByIdUseCase.execute(currentUser, id);
        var response = HospitalWebMapper.toResponse(output);
        log.info("Hospital details retrieved for ID: {}", id);
        return ResponseEntity.ok(response);
    }
}

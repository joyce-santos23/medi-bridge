package br.com.medibridge.medi_bridge.auth.infra.web.controller;

import br.com.medibridge.medi_bridge.auth.core.application.dto.auth.input.LoginInputDTO;
import br.com.medibridge.medi_bridge.auth.core.application.dto.auth.output.LoginOutputDTO;
import br.com.medibridge.medi_bridge.auth.core.application.usecase.LoginUseCase;
import br.com.medibridge.medi_bridge.auth.infra.web.payload.LoginRequestPayload;
import br.com.medibridge.medi_bridge.auth.infra.web.payload.LoginResponsePayload;
import br.com.medibridge.medi_bridge.catalog.infra.web.mapper.user.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponsePayload> login(@Valid @RequestBody LoginRequestPayload request) {
        log.info("REST request to login for email: {}", request.email());
        LoginInputDTO input = new LoginInputDTO(request.email(), request.password());
        LoginOutputDTO output = loginUseCase.execute(input);
        
        LoginResponsePayload response = new LoginResponsePayload(
                output.token(),
                UserWebMapper.toResponse(output.user())
        );
        log.info("REST login successful for email: {}", request.email());
        return ResponseEntity.ok(response);
    }
}

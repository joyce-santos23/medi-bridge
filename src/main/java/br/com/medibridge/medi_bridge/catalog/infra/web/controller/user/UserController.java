package br.com.medibridge.medi_bridge.catalog.infra.web.controller.user;

import br.com.medibridge.medi_bridge.catalog.core.application.security.AuthenticatedUser;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.user.CreateUserUseCase;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.user.GetAllUsersUseCase;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.user.GetUserByIdUseCase;
import br.com.medibridge.medi_bridge.catalog.core.application.usecase.user.UpdateUserUseCase;
import br.com.medibridge.medi_bridge.catalog.infra.web.mapper.user.UserWebMapper;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.RegisterUserRequest;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UpdateUserRequest;
import br.com.medibridge.medi_bridge.catalog.infra.web.payload.user.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody RegisterUserRequest request,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to create user with email: {} for hospital ID: {}", request.email(), request.hospitalId());
        var input = UserWebMapper.toInput(request);
        var output = createUserUseCase.execute(currentUser, input);
        var response = UserWebMapper.toResponse(output);
        log.info("User successfully created with ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to update user profile ID: {}", id);
        var input = UserWebMapper.toInput(id, request);
        var output = updateUserUseCase.execute(currentUser, input);
        var response = UserWebMapper.toResponse(output);
        log.info("User profile ID: {} successfully updated", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to retrieve user profile for ID: {}", id);
        var output = getUserByIdUseCase.execute(currentUser, id);
        var response = UserWebMapper.toResponse(output);
        log.info("User profile retrieved successfully for ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        log.info("Request to list all users for hospital ID: {}", currentUser != null ? currentUser.hospitalId() : "anonymous");
        var outputs = getAllUsersUseCase.execute(currentUser);
        var response = outputs.stream()
                .map(UserWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}

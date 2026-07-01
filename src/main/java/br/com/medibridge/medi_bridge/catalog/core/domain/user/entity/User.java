package br.com.medibridge.medi_bridge.catalog.core.domain.user.entity;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "passwordHash")
public class User {

    @EqualsAndHashCode.Include
    private final UUID id;
    private final UUID hospitalId;
    private String name;
    private String email;
    private ProfessionalCouncil council;
    private String professionalRegistration;
    private Role role;
    private UserStatus status;
    private String passwordHash;

    private User(
            UUID id,
            UUID hospitalId,
            String name,
            String email,
            ProfessionalCouncil council,
            String professionalRegistration,
            Role role,
            UserStatus status,
            String passwordHash
    ) {
        this.id = require(id, "User id is required");
        this.hospitalId = require(hospitalId, "Hospital id is required");
        this.name = requireText(name, "User name is required");
        this.email = requireText(email, "User email is required");
        this.council = require(council, "Professional council is required");
        this.professionalRegistration = requireText(professionalRegistration, "Professional registration is required");
        this.role = require(role, "User role is required");
        this.status = require(status, "User status is required");
        this.passwordHash = requireText(passwordHash, "Password hash is required");
    }

    public static User create(
            UUID hospitalId,
            String name,
            String email,
            ProfessionalCouncil council,
            String professionalRegistration,
            Role role,
            String passwordHash
    ) {
        return new User(
                UUID.randomUUID(),
                hospitalId,
                name,
                email,
                council,
                professionalRegistration,
                role,
                UserStatus.ACTIVE,
                passwordHash
        );
    }

    public static User restore(
            UUID id,
            UUID hospitalId,
            String name,
            String email,
            ProfessionalCouncil council,
            String professionalRegistration,
            Role role,
            UserStatus status,
            String passwordHash
    ) {
        return new User(id, hospitalId, name, email, council, professionalRegistration, role, status, passwordHash);
    }

    public void update(
            String name,
            String email,
            ProfessionalCouncil council,
            String professionalRegistration,
            Role role,
            UserStatus status,
            String passwordHash
    ) {
        this.name = requireText(name, "User name is required");
        this.email = requireText(email, "User email is required");
        this.council = require(council, "Professional council is required");
        this.professionalRegistration = requireText(professionalRegistration, "Professional registration is required");
        this.role = require(role, "User role is required");
        this.status = require(status, "User status is required");
        this.passwordHash = requireText(passwordHash, "Password hash is required");
    }

    public boolean canAuthenticate() {
        return UserStatus.ACTIVE.equals(status);
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
        return value.trim();
    }

    private static <T> T require(T value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
        return value;
    }
}

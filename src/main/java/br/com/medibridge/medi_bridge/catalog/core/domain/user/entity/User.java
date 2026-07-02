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
        this.id = id;
        this.hospitalId = hospitalId;
        this.name = name;
        this.email = email;
        this.council = council;
        this.professionalRegistration = professionalRegistration;
        this.role = role;
        this.status = status;
        this.passwordHash = passwordHash;
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

        require(hospitalId, "Hospital id is required");
        require(name, "User name is required");
        require(email, "User email is required");
        require(council, "Professional council is required");
        require(professionalRegistration, "Professional registration is required");
        require(role, "User role is required");
        require(passwordHash, "Password hash is required");

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
        return new User(
                id,
                hospitalId,
                name,
                email,
                council,
                professionalRegistration,
                role,
                status,
                passwordHash
        );
    }

    public void update(
            String name,
            String email,
            Role role,
            UserStatus status
    ) {

        if (hasText(name)) {
            this.name = name;
        }

        if (hasText(email)) {
            this.email = email;
        }

        if (role != null) {
            this.role = role;
        }

        if (status != null) {
            this.status = status;
        }
    }


    private static <T> T require(T value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
        return value;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

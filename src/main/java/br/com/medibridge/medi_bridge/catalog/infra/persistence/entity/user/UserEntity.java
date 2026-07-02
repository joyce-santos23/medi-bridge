package br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.user;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.ProfessionalCouncil;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.Role;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.enums.UserStatus;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital.HospitalEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_hospital", columnList = "hospital_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "uk_user_professional_registration",
                        columnNames = {
                                "council",
                                "professional_registration"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfessionalCouncil council;

    @Column(name = "professional_registration", nullable = false)
    private String professionalRegistration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private String passwordHash;
}
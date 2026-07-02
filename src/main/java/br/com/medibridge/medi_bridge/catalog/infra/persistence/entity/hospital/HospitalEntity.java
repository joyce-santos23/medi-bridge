package br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.address.AddressBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "hospitals",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_hospital_cnpj",
                        columnNames = "cnpj"
                ),
                @UniqueConstraint(
                        name = "uk_hospital_cnes",
                        columnNames = "cnes"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private String cnes;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_base_id", nullable = false)
    private AddressBaseEntity addressBase;

    @Column(nullable = false)
    private String number;

    @Column
    private String complement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HospitalStatus status;
}
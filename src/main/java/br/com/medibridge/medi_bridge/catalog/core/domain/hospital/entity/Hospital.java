package br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.catalog.core.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Address;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Hospital {

    @EqualsAndHashCode.Include
    private final UUID id;
    private String name;
    private Cnpj cnpj;
    private String cnes;
    private String email;
    private String phone;
    private Address address;
    private HospitalStatus status;

    private Hospital(
            UUID id,
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            Address address,
            HospitalStatus status
    ) {
        this.id = require(id, "Hospital id is required");
        this.name = requireText(name, "Hospital name is required");
        this.cnpj = require(cnpj, "Hospital CNPJ is required");
        this.cnes = requireText(cnes, "Hospital CNES is required");
        this.email = requireText(email, "Hospital email is required");
        this.phone = requireText(phone, "Hospital phone is required");
        this.address = require(address, "Hospital address is required");
        this.status = require(status, "Hospital status is required");
    }

    public static Hospital create(
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            Address address
    ) {
        return new Hospital(UUID.randomUUID(), name, cnpj, cnes, email, phone, address, HospitalStatus.ACTIVE);
    }

    public static Hospital restore(
            UUID id,
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            Address address,
            HospitalStatus status
    ) {
        return new Hospital(id, name, cnpj, cnes, email, phone, address, status);
    }

    public void update(
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            Address address,
            HospitalStatus status
    ) {
        this.name = requireText(name, "Hospital name is required");
        this.cnpj = require(cnpj, "Hospital CNPJ is required");
        this.cnes = requireText(cnes, "Hospital CNES is required");
        this.email = requireText(email, "Hospital email is required");
        this.phone = requireText(phone, "Hospital phone is required");
        this.address = require(address, "Hospital address is required");
        this.status = require(status, "Hospital status is required");
    }

    public boolean isActive() {
        return HospitalStatus.ACTIVE.equals(status);
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

package br.com.medibridge.medi_bridge.catalog.core.domain.hospital.entity;

import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.enums.HospitalStatus;
import br.com.medibridge.medi_bridge.shared.domain.exception.ValidationException;
import br.com.medibridge.medi_bridge.catalog.core.domain.hospital.valueobject.Cnpj;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hospital {

    @EqualsAndHashCode.Include
    private final UUID id;
    private String name;
    private Cnpj cnpj;
    private String cnes;
    private String email;
    private String phone;
    private UUID addressBaseId;
    private String number;
    private String complement;
    private HospitalStatus status;

    private Hospital(
            UUID id,
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            UUID addressBaseId,
            String number,
            String complement,
            HospitalStatus status
    ) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.cnes = cnes;
        this.email = email;
        this.phone = phone;
        this.addressBaseId = addressBaseId;
        this.number = number;
        this.complement = complement;
        this.status = status;
    }

    public static Hospital create(
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            UUID addressBaseId,
            String number,
            String complement
    ) {
        require(name, "Hospital name is required");
        require(cnpj, "Hospital CNPJ is required");
        require(cnes, "Hospital CNES is required");
        require(email, "Hospital email is required");
        require(phone, "Hospital phone is required");
        require(addressBaseId, "Hospital addressBaseId is required");
        requireText(number, "Hospital number is required");

        return new Hospital(
                UUID.randomUUID(),
                name,
                cnpj,
                cnes,
                email,
                phone,
                addressBaseId,
                number,
                normalize(complement),
                HospitalStatus.ACTIVE
        );
    }

    public static Hospital restore(
            UUID id,
            String name,
            Cnpj cnpj,
            String cnes,
            String email,
            String phone,
            UUID addressBaseId,
            String number,
            String complement,
            HospitalStatus status
        ) {
            require(id, "Hospital id is required");
            require(status, "Hospital status is required");
    
            return new Hospital(
                    id,
                    name,
                    cnpj,
                    cnes,
                    email,
                    phone,
                    addressBaseId,
                    number,
                    normalize(complement),
                    status
            );
        }
    
        public void update(
                String email,
                String phone,
                HospitalStatus status
        ) {
            if (hasText(email)) {
                this.email = email;
            }
    
            if (hasText(phone)) {
                this.phone = phone;
            }
    
            if (status != null) {
                this.status = status;
            }
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
    
        private static boolean hasText(String value) {
            return value != null && !value.isBlank();
        }

        private static String normalize(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            return value.trim();
        }
    }

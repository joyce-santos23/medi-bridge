package br.com.medibridge.medi_bridge.catalog.infra.persistence.mapper.user;

import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital.HospitalEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.user.UserEntity;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static UserEntity toEntity(User domain, HospitalEntity hospital) {

        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setHospital(hospital);
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setCouncil(domain.getCouncil());
        entity.setProfessionalRegistration(domain.getProfessionalRegistration());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        entity.setPasswordHash(domain.getPasswordHash());

        return entity;
    }

    public static void updateEntity(
            UserEntity entity,
            User domain
    ) {
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
    }

    public static User toDomain(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        return User.restore(
                entity.getId(),
                entity.getHospital().getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCouncil(),
                entity.getProfessionalRegistration(),
                entity.getRole(),
                entity.getStatus(),
                entity.getPasswordHash()
        );
    }
}
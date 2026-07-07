package br.com.medibridge.medi_bridge.catalog.infra.persistence.adapter.user;

import br.com.medibridge.medi_bridge.catalog.core.application.port.user.UserGateway;
import br.com.medibridge.medi_bridge.catalog.core.domain.user.entity.User;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.hospital.HospitalEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.entity.user.UserEntity;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.mapper.user.UserPersistenceMapper;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.hospital.HospitalRepository;
import br.com.medibridge.medi_bridge.catalog.infra.persistence.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserGateway {

    private final UserRepository repository;
    private final HospitalRepository hospitalRepository;

    @Override
    public User save(User user) {

        Optional<UserEntity> existing = repository.findById(user.getId());

        UserEntity entity;

        if (existing.isPresent()) {

            entity = existing.get();

            UserPersistenceMapper.updateEntity(entity, user);

        } else {

            HospitalEntity hospital =
                    hospitalRepository.getReferenceById(user.getHospitalId());

            entity = UserPersistenceMapper.toEntity(
                    user,
                    hospital
            );
        }

        UserEntity saved = repository.save(entity);

        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public java.util.List<User> findAllByHospitalId(UUID hospitalId) {
        return repository.findAllByHospitalId(hospitalId)
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }
}
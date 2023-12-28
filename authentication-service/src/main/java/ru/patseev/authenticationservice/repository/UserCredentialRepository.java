package ru.patseev.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.authenticationservice.domain.UserEntity;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<UserEntity> findByActivationCode(String activationCode);
}

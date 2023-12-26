package ru.patseev.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.authenticationservice.domain.UserCredential;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {

	Optional<UserCredential> findByUsername(String username);

	boolean existsByUsername(String username);
}

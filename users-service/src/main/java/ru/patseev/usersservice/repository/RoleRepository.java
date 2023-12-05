package ru.patseev.usersservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.usersservice.domain.RoleEntity;
import ru.patseev.usersservice.dto.UserRoles;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

	Optional<RoleEntity> findByRoleName(UserRoles role);
}

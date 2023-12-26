package ru.patseev.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.authenticationservice.domain.Role;
import ru.patseev.authenticationservice.domain.UserRoles;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role getRoleByRoleName(UserRoles role);
}

package ru.patseev.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.authenticationservice.domain.Role;
import ru.patseev.authenticationservice.domain.UserRoles;

/**
 * Репозиторий отвечающий за работу с ролями пользователей.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	/**
	 * Получает роль по её имени.
	 *
	 * @param role Имя роли для поиска.
	 * @return Возвращает объект Role с найденной ролью, если существует.
	 */
	Role getRoleByRoleName(UserRoles role);
}

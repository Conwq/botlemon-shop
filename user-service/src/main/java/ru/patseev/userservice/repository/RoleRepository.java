package ru.patseev.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.userservice.domain.RoleEntity;
import ru.patseev.userservice.domain.UserRoles;

/**
 * Репозиторий отвечающий за работу с ролями пользователей.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

	/**
	 * Получает роль по её имени.
	 *
	 * @param role Имя роли для поиска.
	 * @return Возвращает объект Role с найденной ролью, если существует.
	 */
	RoleEntity getRoleByRoleName(UserRoles role);
}

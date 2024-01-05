package ru.patseev.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.authenticationservice.domain.UserEntity;

import java.util.Optional;

/**
 * Репозиторий отвечающий за получение данных из базы данных.
 */
@Repository
public interface UserCredentialRepository extends JpaRepository<UserEntity, Integer> {

	/**
	 * Поиск пользователя по имени пользователя.
	 *
	 * @param username Имя искомого пользователя.
	 * @return Возвращает объект Optional<UserEntity> с найденным пользователем, если существует.
	 */
	Optional<UserEntity> findByUsername(String username);

	/**
	 * Проверяет наличие пользователя с указанным именем пользователя.
	 *
	 * @param username Имя пользователя для проверки.
	 * @return Возвращает true, если пользователь существует, в противном случае false.
	 */
	boolean existsByUsername(String username);

	/**
	 * Проверяет наличие пользователя с указанным email.
	 *
	 * @param email Email пользователя для проверки.
	 * @return Возвращает true, если пользователь существует, в противном случае false.
	 */
	boolean existsByEmail(String email);

	/**
	 * Поиск пользователя по коду активации.
	 *
	 * @param activationCode Код активации для поиска пользователя.
	 * @return Возвращает объект Optional<UserEntity> с найденным пользователем, если существует.
	 */
	Optional<UserEntity> findByActivationCode(String activationCode);
}
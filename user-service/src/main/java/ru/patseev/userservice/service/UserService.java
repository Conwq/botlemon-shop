package ru.patseev.userservice.service;

import ru.patseev.userservice.dto.UserDto;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {
	/**
	 * Сохраняет информацию о новом пользователе.
	 *
	 * @param userDto DTO пользователя для сохранения.
	 */
	void saveUser(UserDto userDto);

	/**
	 * Получает учетные данные пользователя по его имени пользователя.
	 *
	 * @param username Имя пользователя.
	 * @return Объект Optional, содержащий учетные данные пользователя (если существует).
	 */
	Optional<UserDto> getUserCredentials(String username);

	/**
	 * Проверяет существование пользователя по его имени пользователя.
	 *
	 * @param username Имя пользователя.
	 * @return true, если пользователь существует, в противном случае - false.
	 */
	boolean checkUserExistenceByUsername(String username);

	/**
	 * Проверяет существование пользователя по его адресу электронной почты.
	 *
	 * @param email Адрес электронной почты пользователя.
	 * @return true, если пользователь существует, в противном случае - false.
	 */
	boolean checkUserExistenceByEmail(String email);

	/**
	 * Активирует учетную запись пользователя по коду активации.
	 *
	 * @param activationCode Код активации.
	 * @return true, если активация прошла успешно, в противном случае - false.
	 */
	boolean activateAccount(String activationCode);
}
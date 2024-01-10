package ru.patseev.authenticationservice.service;

import org.springframework.http.ResponseEntity;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.dto.RegisterRequest;

/**
 * Сервис отвечающий для работы с авторизацией и регистрацией пользователя
 */
public interface AuthenticationService {

	/**
	 * Регистрация и сохранение пользователя в БД.
	 * Через сторонний клиент отправляет подтверждающий код.
	 *
	 * @param request Запрос на регистрацию пользователя.
	 */
	void registerUser(RegisterRequest request);

	/**
	 * Аутентифицирует и авторизует пользователя.
	 *
	 * @param request Запрос на авторизацию пользователя.
	 * @return Возвращает объект с токеном.
	 */
	AuthResponse authUser(AuthRequest request);

	/**
	 * Активирует аккаунт.
	 *
	 * @param activationCode Код активации.
	 * @return Сообщение об активации аккаунта
	 */
	ResponseEntity<?> activateAccount(String activationCode);
}
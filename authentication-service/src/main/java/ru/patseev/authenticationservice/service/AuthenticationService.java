package ru.patseev.authenticationservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.dto.RegisterRequest;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;

/**
 * Сервис отвечающий за работу с авторизацией и регистрацией пользователя
 */
public interface AuthenticationService {

	/**
	 * Регистрирует и сохраняет пользователя в базе данных.
	 *
	 * @param request Запрос на регистрацию пользователя.
	 * @throws UserAlreadyExistException если пользователь уже существует.
	 */
	void registerUser(RegisterRequest request);

	/**
	 * Аутентифицирует и авторизует пользователя.
	 *
	 * @param request Запрос на авторизацию пользователя.
	 * @return Возвращает объект с токеном.
	 * @throws UsernameNotFoundException если пользователь не найден.
	 */
	AuthResponse authUser(AuthRequest request);

	/**
	 * Активирует аккаунт пользователя.
	 *
	 * @param activationCode Код активации.
	 * @throws UsernameNotFoundException если пользователь не найден.
	 */
	ResponseEntity<?> activateAccount(String activationCode);
}
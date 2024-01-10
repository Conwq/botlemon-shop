package ru.patseev.authenticationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.dto.RegisterRequest;
import ru.patseev.authenticationservice.service.AuthenticationService;

/**
 * Контроллер по работе с регистрацией и авторизацией пользователя.
 */
@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authorizationService;

	/**
	 * Регистрирует пользователя в базе данных.
	 *
	 * @param request Запрос на регистрацию пользователя.
	 * @return Возвращает объект при успешной регистрации.
	 */
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid
											   @RequestBody RegisterRequest request) {
		authorizationService.registerUser(request);
		return ResponseEntity.ok("Registration successfully");
	}

	/**
	 * Авторизирует пользователя.
	 *
	 * @param request Запрос на авторизации.
	 * @return Возвращает объект с авторизацией пользователя.
	 */
	@PostMapping("/authorization")
	public ResponseEntity<?> authUser(@Valid
									  @RequestBody AuthRequest request) {
		AuthResponse response = authorizationService.authUser(request);
		return ResponseEntity.ok(response.token());
	}

	/**
	 * Активирует аккаунт пользователя.
	 *
	 * @param activationCode Код активации.
	 * @return Сообщние об успешной активации аккаунта.
	 */
	@GetMapping("/activate/{uuid}")
	public ResponseEntity<String> activateAccount(@PathVariable("uuid") String activationCode) {
		String message = authorizationService.activateAccount(activationCode);
		return ResponseEntity.ok(message);
	}
}
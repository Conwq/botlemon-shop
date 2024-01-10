package ru.patseev.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.patseev.userservice.dto.UserDto;
import ru.patseev.userservice.service.UserService;

import java.util.Optional;

/**
 * Контроллер, который хранит в себе конечные точки по работе с пользователями.
 */
@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	/**
	 * Сохраняет пользователя.
	 *
	 * @param userDto Запрос на сохранение пользователя.
	 */
	@PostMapping("/save")
	public void saveUser(@RequestBody UserDto userDto) {
		userService.saveUser(userDto);
	}

	/**
	 * Получение данных пользователя.
	 *
	 * @param username Имя пользователя, данные которого нужно получить.
	 * @return Возвращает объект Optional<UserDto>, который хранит (или нет) в себе данные.
	 */
	@GetMapping("/user/{username}")
	public Optional<UserDto> getUserCredential(@PathVariable String username) {
		return userService.getUserCredentials(username);
	}

	/**
	 * Проверяет существование такого имени пользователя.
	 *
	 * @param username Имя пользователя, которое нужно проверить.
	 * @return Возвращает значение true или false.
	 */
	@GetMapping("/check_username")
	public Boolean checkUserExistenceByUsername(@RequestParam String username) {
		return userService.checkUserExistenceByUsername(username);
	}

	/**
	 * Проверяет существование такого имейла.
	 *
	 * @param email Имейл, который нужно проверить.
	 * @return Возвращает значение true или false.
	 */
	@GetMapping("/check_email")
	public Boolean checkUserExistenceByEmail(@RequestParam String email) {
		return userService.checkUserExistenceByEmail(email);
	}

	/**
	 * Активирует аккаунт.
	 *
	 * @param activationCode Код активации.
	 * @return Возвращает значение true или false.
	 */
	@GetMapping("/activate_account/{activationCode}")
	public Boolean activateAccount(@PathVariable String activationCode) {
		return userService.activateAccount(activationCode);
	}
}
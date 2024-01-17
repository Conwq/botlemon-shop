package ru.patseev.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.accountservice.dto.AccountDto;
import ru.patseev.accountservice.service.AccountService;
import ru.patseev.jwtservice.starter.service.JwtHeader;
import ru.patseev.jwtservice.starter.service.JwtService;

/**
 * Контроллер для управления аккаунтами пользователей.
 */
@RestController
@RequestMapping("/v1/api/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	private final JwtService jwtService;

	/**
	 * Получение деталей аккаунта пользователя.
	 *
	 * @param header Заголовок с токеном авторизации.
	 * @return Детали аккаунта пользователя в виде {@link AccountDto}.
	 */
	@GetMapping("/details")
	public ResponseEntity<AccountDto> getUserAccountDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
		String token = header.replace(JwtHeader.BEARER, "");
		String username = jwtService.extractUsername(token);
		AccountDto userAccountDetails = accountService.getUserAccountDetails(username);
		return ResponseEntity.ok(userAccountDetails);
	}

	/**
	 * Добавление деталей аккаунта для указанного пользователя.
	 *
	 * @param userId Идентификатор пользователя.
	 * @return Ответ с кодом 200 OK в случае успешного добавления, иначе 400 Bad Request.
	 */
	@PostMapping("/create/{userId}")
	public ResponseEntity<Object> addAccountDetails(@PathVariable int userId) {
		if (accountService.addAccountDetails(userId)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}

	/**
	 * Обновление даты последнего входа для указанного пользователя.
	 *
	 * @param userId Идентификатор пользователя.
	 * @return Ответ с кодом 200 OK в случае успешного обновления, иначе 400 Bad Request.
	 */
	@PatchMapping("/update_login_date/{userId}")
	public ResponseEntity<Object> updateLastLoginDate(@PathVariable int userId) {
		if (accountService.updateLastLoginDate(userId)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
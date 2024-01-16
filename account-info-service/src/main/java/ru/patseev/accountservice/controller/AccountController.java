package ru.patseev.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.accountservice.dto.AccountDto;
import ru.patseev.accountservice.service.AccountService;
import ru.patseev.jwtservice.starter.service.JwtHeader;
import ru.patseev.jwtservice.starter.service.JwtService;

@RestController
@RequestMapping("/v1/api/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	private final JwtService jwtService;

	@GetMapping("/details")
	public ResponseEntity<AccountDto> getUserAccountDetails(@RequestHeader(HttpHeaders.AUTHORIZATION)
															String header) {
		String token = header.replace(JwtHeader.BEARER, "");
		String username = jwtService.extractUsername(token);
		AccountDto userAccountDetails = accountService.getUserAccountDetails(username);
		return ResponseEntity.ok(userAccountDetails);
	}

	@PostMapping("/create/{userId}")
	public ResponseEntity<Object> addAccountDetails(@PathVariable int userId) {
		accountService.addAccountDetails(userId);
		return ResponseEntity.ok().build();
	}
}
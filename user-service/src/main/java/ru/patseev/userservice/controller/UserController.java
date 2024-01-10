package ru.patseev.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.patseev.userservice.dto.UserDto;
import ru.patseev.userservice.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/save")
	public void saveUser(@RequestBody UserDto userDto) {
		userService.saveUser(userDto);
	}

	@GetMapping("/user/{username}")
	public Optional<UserDto> getUserCredential(@PathVariable String username) {
		return userService.getUserCredentials(username);
	}

	@GetMapping("/check_username")
	public Boolean checkUserExistenceByUsername(@RequestParam String username) {
		return userService.checkUserExistenceByUsername(username);
	}

	@GetMapping("/check_email")
	public Boolean checkUserExistenceByEmail(@RequestParam String email) {
		return userService.checkUserExistenceByEmail(email);
	}

	@GetMapping("/activate_account/{activationCode}")
	public Boolean activateAccount(@PathVariable String activationCode) {
		return userService.activateAccount(activationCode);
	}
}
package ru.patseev.usersservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.patseev.usersservice.dto.AuthorizationResponse;
import ru.patseev.usersservice.dto.UserDto;
import ru.patseev.usersservice.service.UserService;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/registration")
	public UserDto registrationUser(@RequestBody UserDto userDto) {
		return userService.registrationUser(userDto);
	}

	@PostMapping("/authorization")
	public AuthorizationResponse authorizationUser(@RequestBody UserDto userDto) {
		return userService.authorizationUser(userDto);
	}
}
package ru.patseev.authenticationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.authenticationservice.dto.AuthenticationRequest;
import ru.patseev.authenticationservice.dto.AuthenticationResponse;
import ru.patseev.authenticationservice.service.AuthenticationService;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authorizationService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationRequest request) {
		AuthenticationResponse response = authorizationService.saveUser(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/authorization")
	public ResponseEntity<AuthenticationResponse> authUser(@RequestBody AuthenticationRequest request) {
		AuthenticationResponse response = authorizationService.authUser(request);
		return ResponseEntity.ok(response);
	}
}
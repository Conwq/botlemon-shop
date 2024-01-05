package ru.patseev.authenticationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.dto.RegisterRequest;
import ru.patseev.authenticationservice.service.AuthenticationService;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authorizationService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
		authorizationService.registerUser(request);
		return ResponseEntity.ok("Registration successfully");
	}

	@PostMapping("/authorization")
	public ResponseEntity<AuthResponse> authUser(@Valid @RequestBody AuthRequest request) {
		AuthResponse response = authorizationService.authUser(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/activate/{uuid}")
	public ResponseEntity<String> activateAccount(@PathVariable("uuid") String activationCode) {
		authorizationService.activateAccount(activationCode);
		return ResponseEntity.ok("Email confirmed");
	}
}
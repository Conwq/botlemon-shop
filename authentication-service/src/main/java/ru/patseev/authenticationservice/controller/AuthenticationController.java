package ru.patseev.authenticationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.service.AuthenticationService;
import ru.patseev.authenticationservice.service.JwtService;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authorizationService;
	private final JwtService jwtService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody AuthRequest request) {
		authorizationService.registerUser(request);
		return ResponseEntity.ok("Registration successfully");
	}

	@PostMapping("/authorization")
	public ResponseEntity<AuthResponse> authUser(@RequestBody AuthRequest request) {
		AuthResponse response = authorizationService.authUser(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/admin")
	public String admin() {
		return "Only admin";
	}

	@GetMapping("/all")
	public String allUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
		System.out.println(header);
		String token = header.substring(7);

		System.out.println(jwtService.extractUserId(token));
		System.out.println(jwtService.extractUsername(token));
		System.out.println(jwtService.extractUserRole(token));

		return "All user";
	}
}
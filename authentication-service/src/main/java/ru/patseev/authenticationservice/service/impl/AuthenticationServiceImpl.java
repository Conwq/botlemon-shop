package ru.patseev.authenticationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.patseev.authenticationservice.client.AccountServiceClient;
import ru.patseev.authenticationservice.client.EmailSenderClient;
import ru.patseev.authenticationservice.client.UserServiceClient;
import ru.patseev.authenticationservice.dto.*;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;
import ru.patseev.authenticationservice.service.AuthenticationService;
import ru.patseev.jwtservice.starter.service.JwtService;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EmailSenderClient emailSenderClient;
	private final UserServiceClient userServiceClient;
	private final AccountServiceClient accountServiceClient;

	@Override
	public void registerUser(RegisterRequest request) {
		this.checkForUserExistence(request);

		UserDto dto = this.createDto(request);
		userServiceClient.sendUserDataForSaving(dto);
		emailSenderClient.sendEmailConfirmingAccountToUser(
				dto.email(),
				dto.activationCode()
		);
	}

	@Override
	public AuthResponse authUser(AuthRequest request) {
		UserDto userCredential = userServiceClient
				.sendRequestToReceiveUserCredentials(request.username())
				.filter(UserDto::enabled)
				.filter(user -> passwordEncoder.matches(request.password(), user.password()))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String token = this.generateJsonWebToken(userCredential);
		accountServiceClient.sendRequestToUpdateLastLoginDate(userCredential.id());
		return new AuthResponse(token);
	}

	@Override
	public ResponseEntity<?> activateAccount(String activationCode) {
		boolean isActivated = userServiceClient.sendRequestToActivateAccount(activationCode);
		if (isActivated) {
			return ResponseEntity.ok("Activated");
		}
		return ResponseEntity.badRequest().build();
	}

	/*
	 * Создает и возвращает JWT token.
	 */
	private String generateJsonWebToken(UserDto userCredential) {
		return jwtService.generateToken(
				this.createExtraClaims(userCredential),
				userCredential.username()
		);
	}

	/*
	 * Создает дополнительные условия в токен.
	 */
	private Map<String, Object> createExtraClaims(UserDto userCredential) {
		return Map.of(
				"userId", userCredential.id(),
				"role", userCredential.role().name()
		);
	}

	//TODO использовать маппер?
	/*
	 * Метод, который создает DTO
	 */
	private UserDto createDto(RegisterRequest request) {
		String activationCode = UUID.randomUUID().toString();

		return UserDto.builder()
				.email(request.email())
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.firstName(request.firstName())
				.lastName(request.lastName())
				.role(UserRoles.USER)
				.activationCode(activationCode)
				.build();
	}

	/*
	 * Проверка на существование пользователя
	 */
	private void checkForUserExistence(RegisterRequest request) {
		if (userServiceClient.sendRequestToCheckExistenceUsername(request.username()) ||
				userServiceClient.sendRequestToCheckExistenceEmail(request.email())) {
			throw new UserAlreadyExistException("This user already exist");
		}
	}
}
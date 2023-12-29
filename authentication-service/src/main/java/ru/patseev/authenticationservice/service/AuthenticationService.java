package ru.patseev.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.authenticationservice.client.EmailSenderClient;
import ru.patseev.authenticationservice.domain.Role;
import ru.patseev.authenticationservice.domain.UserEntity;
import ru.patseev.authenticationservice.domain.UserRoles;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;
import ru.patseev.authenticationservice.repository.RoleRepository;
import ru.patseev.authenticationservice.repository.UserCredentialRepository;
import ru.patseev.jwtservice.starter.service.JwtService;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserCredentialRepository userCredentialRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EmailSenderClient emailSenderClient;

	@Transactional
	public void registerUser(AuthRequest request) {
		if (userCredentialRepository.existsByUsername(request.username()) ||
				userCredentialRepository.existsByEmail(request.email())) {
			throw new UserAlreadyExistException("This user already exist");
		}
		UserEntity userEntity = this.mapToEntity(request);
		userCredentialRepository.save(userEntity);

		emailSenderClient.sendEmailConfirmingAccountToUser(
				userEntity.getEmail(),
				userEntity.getActivationCode());
	}

	@Transactional(readOnly = true)
	public AuthResponse authUser(AuthRequest request) {
		UserEntity userCredential = userCredentialRepository
				.findByUsername(request.username())
				.filter(UserEntity::isEnabled)
				.filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String token = this.generateJsonWebToken(userCredential);

		return new AuthResponse(token);
	}

	@Transactional
	public void activateAccount(String activationCode) {
		UserEntity userEntity = userCredentialRepository
				.findByActivationCode(activationCode)
				.orElseThrow(() -> new UsernameNotFoundException(""));

		userEntity.setEnabled(true);
		userEntity.setActivationCode(null);

		userCredentialRepository.save(userEntity);
	}

	private UserEntity mapToEntity(AuthRequest request) {
		Role role = roleRepository.getRoleByRoleName(UserRoles.USER);
		String activationCode = UUID.randomUUID().toString();

		return UserEntity.builder()
				.email(request.email())
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.firstName(request.firstName())
				.lastName(request.lastName())
				.role(role)
				.activationCode(activationCode)
				.build();
	}

	public String generateJsonWebToken(UserEntity userCredential) {
		return jwtService.generateToken(
				this.createExtraClaims(userCredential),
				userCredential.getUsername()
		);
	}

	private Map<String, Object> createExtraClaims(UserEntity userCredential) {
		return Map.of(
				"userId", userCredential.getId(),
				"role", userCredential.getRole().getRoleName().name()
		);
	}
}
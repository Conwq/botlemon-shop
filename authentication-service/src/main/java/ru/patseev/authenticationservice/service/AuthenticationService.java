package ru.patseev.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.authenticationservice.domain.Role;
import ru.patseev.authenticationservice.domain.UserCredential;
import ru.patseev.authenticationservice.domain.UserRoles;
import ru.patseev.authenticationservice.dto.AuthRequest;
import ru.patseev.authenticationservice.dto.AuthResponse;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;
import ru.patseev.authenticationservice.repository.RoleRepository;
import ru.patseev.authenticationservice.repository.UserCredentialRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserCredentialRepository userCredentialRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	//TODO (enabled) Делать доступность аккаунта только после того, как пользователь подтвердит адрес эл.почты
	@Transactional
	public void registerUser(AuthRequest request) {
		if (userCredentialRepository.existsByUsername(request.username())) {
			throw new UserAlreadyExistException("This user already exist");
		}
		UserCredential userCredential = this.mapToEntity(request);
		userCredentialRepository.save(userCredential);
	}

	@Transactional(readOnly = true)
	public AuthResponse authUser(AuthRequest request) {
		//TODO тут выкидывается просто 403, если пользователь ввел неверные данные
		UserCredential userCredential = userCredentialRepository
				.findByUsername(request.username())
				.filter(UserCredential::isEnabled)
				.filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String token = this.generateJsonWebToken(userCredential);

		return new AuthResponse(token);
	}

	private UserCredential mapToEntity(AuthRequest request) {
		Role role = roleRepository.getRoleByRoleName(UserRoles.USER);

		return UserCredential.builder()
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.firstName(request.firstName())
				.lastName(request.lastName())
				.role(role)
				.build();
	}

	public String generateJsonWebToken(UserCredential userCredential) {
		return jwtService.generateToken(
				this.createExtraClaims(userCredential),
				userCredential.getUsername()
		);
	}

	private Map<String, Object> createExtraClaims(UserCredential userCredential) {
		return Map.of(
				"id", userCredential.getId(),
				"role", userCredential.getRole().getRoleName().name()
		);
	}
}
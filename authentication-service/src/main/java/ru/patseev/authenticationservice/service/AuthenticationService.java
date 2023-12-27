package ru.patseev.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.authenticationservice.domain.Role;
import ru.patseev.authenticationservice.domain.UserCredential;
import ru.patseev.authenticationservice.domain.UserRoles;
import ru.patseev.authenticationservice.dto.AuthenticationRequest;
import ru.patseev.authenticationservice.dto.AuthenticationResponse;
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
	private final AuthenticationManager authenticationManager;

	//TODO (enabled) Делать доступность аккаунта только после того, как пользователь подтвердит адрес эл.почты
	@Transactional
	public AuthenticationResponse registerUser(AuthenticationRequest request) {

		if (userCredentialRepository.existsByUsername(request.username())) {
			throw new UserAlreadyExistException("This user already exist");
		}
		UserCredential userCredential = this.mapToEntity(request);
		userCredentialRepository.save(userCredential);

		String token = jwtService.generateToken(
				this.createExtraClaims(userCredential),
				userCredential.getUsername()
		);

		return new AuthenticationResponse(token);
	}

	@Transactional(readOnly = true)
	public AuthenticationResponse authUser(AuthenticationRequest request) {
		System.out.println(3);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				request.username(),
				request.password()
		);
		//TODO тут выкидывается просто 403, если пользователь ввел неверные данные
		authenticationManager.authenticate(authentication);
		System.out.println(1);
		UserCredential userCredential = userCredentialRepository
				.findByUsername(request.username())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		System.out.println(2);
		String token = jwtService.generateToken(
				this.createExtraClaims(userCredential),
				userCredential.getUsername()
		);

		return new AuthenticationResponse(token);
	}

	private Map<String, Object> createExtraClaims(UserCredential userCredential) {
		return Map.of(
				"id", userCredential.getId(),
				"role", userCredential.getRole().getRoleName().name()
		);
	}

	private UserCredential mapToEntity(AuthenticationRequest request) {
		Role role = roleRepository.getRoleByRoleName(UserRoles.USER);

		return UserCredential.builder()
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.firstName(request.firstName())
				.lastName(request.lastName())
				.role(role)
				.build();
	}
}
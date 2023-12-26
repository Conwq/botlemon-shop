package ru.patseev.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
	public AuthenticationResponse saveUser(AuthenticationRequest request) {
		Role role = roleRepository.getRoleByRoleName(UserRoles.USER);

		if (userCredentialRepository.existsByUsername(request.username())) {
			throw new UserAlreadyExistException("This user already exist");
		}

		UserCredential userCredential = UserCredential.builder()
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.firstName(request.firstName())
				.lastName(request.lastName())
				.role(role)
				.build();


		userCredentialRepository.save(userCredential);

		String jwtToken = jwtService.generateToken(request.username());
		return new AuthenticationResponse(jwtToken);
	}

	@Transactional(readOnly = true)
	public AuthenticationResponse authUser(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.username(),
						request.password()
				)
		);

		String jwtToken = jwtService.generateToken(request.username());
		return new AuthenticationResponse(jwtToken);
	}
}
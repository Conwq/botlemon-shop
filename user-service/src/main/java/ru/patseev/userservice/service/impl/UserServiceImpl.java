package ru.patseev.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.userservice.client.AccountServiceClient;
import ru.patseev.userservice.domain.RoleEntity;
import ru.patseev.userservice.domain.UserEntity;
import ru.patseev.userservice.dto.UserDto;
import ru.patseev.userservice.repository.RoleRepository;
import ru.patseev.userservice.repository.UserRepository;
import ru.patseev.userservice.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AccountServiceClient accountServiceClient;

	@Override
	@Transactional
	public void saveUser(UserDto userDto) {
		UserEntity userEntity = this.mapToEntity(userDto);
		userRepository.save(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserCredentials(String username) {
		return userRepository
				.findByUsername(username)
				.map(this::mapToDto);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkUserExistenceByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkUserExistenceByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional
	public boolean activateAccount(String activationCode) {
		Optional<UserEntity> optionalUser = userRepository.findByActivationCode(activationCode);

		if (optionalUser.isPresent()) {
			UserEntity userEntity = optionalUser.get();
			userEntity.setEnabled(true);
			userEntity.setActivationCode(null);

			userRepository.save(userEntity);
			accountServiceClient.sendRequestToCreatePersonalAccountDetails(userEntity.getId());
			return true;
		}
		return false;
	}

	/*
	 * Преобразует dto в entity
	 */
	private UserEntity mapToEntity(UserDto dto) {
		RoleEntity role = roleRepository.getRoleByRoleName(dto.role());

		return UserEntity.builder()
				.email(dto.email())
				.username(dto.username())
				.password(dto.password())
				.firstName(dto.firstName())
				.lastName(dto.lastName())
				.role(role)
				.activationCode(dto.activationCode())
				.build();
	}

	/*
	 * Преобразует entity в dto
	 */
	private UserDto mapToDto(UserEntity entity) {
		return new UserDto(
				entity.getId(),
				entity.getEmail(),
				entity.getUsername(),
				entity.getPassword(),
				entity.getFirstName(),
				entity.getLastName(),
				entity.getRole().getRoleName(),
				entity.getRegistrationAt(),
				entity.getActivationCode(),
				entity.isEnabled()
		);
	}
}
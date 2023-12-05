package ru.patseev.usersservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.patseev.usersservice.domain.RoleEntity;
import ru.patseev.usersservice.domain.UserEntity;
import ru.patseev.usersservice.dto.AuthorizationResponse;
import ru.patseev.usersservice.dto.UserRoles;
import ru.patseev.usersservice.dto.UserDto;
import ru.patseev.usersservice.exception.IncorrectInputDataException;
import ru.patseev.usersservice.exception.RoleNotFoundException;
import ru.patseev.usersservice.exception.UserAlreadyExistException;
import ru.patseev.usersservice.exception.UserNotFoundException;
import ru.patseev.usersservice.mapper.UserMapper;
import ru.patseev.usersservice.repository.RoleRepository;
import ru.patseev.usersservice.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final UserMapper userMapper;

	public UserDto registrationUser(UserDto userDto) {

		this.checkUserExisted(userDto);
		UserEntity userEntity = this.createUserEntity(userDto);

		return userMapper.toDto(userRepository.save(userEntity));
	}

	public AuthorizationResponse authorizationUser(UserDto userDto) {

		return userRepository
				.findByUsername(userDto.getUsername())
				.map(user -> {
					if(!user.getPassword().equals(userDto.getPassword())) {
						throw new IncorrectInputDataException("Incorrect password");
					}
					return AuthorizationResponse.builder()
							.message("Authorization successfully")
							.token("Token")
							.username(user.getUsername())
							.build();
				})
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	private UserEntity createUserEntity(UserDto userDto) {
		RoleEntity roleEntity = getRoleEntity();
		UserEntity userEntity = userMapper.toEntity(userDto);

		userEntity.setRoleEntity(roleEntity);
		userEntity.setRegistrationAt(Timestamp.from(Instant.now()));

		return userEntity;
	}

	private RoleEntity getRoleEntity() {
		return roleRepository
				.findByRoleName(UserRoles.USER)
				.orElseThrow(() -> new RoleNotFoundException("Role not found."));
	}

	private void checkUserExisted(UserDto userDto) {
		userRepository
				.findByUsername(userDto.getUsername())
				.ifPresent(userEntity -> {
					throw new UserAlreadyExistException("This user is already registered.");
				});
	}
}
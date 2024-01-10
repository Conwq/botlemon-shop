package ru.patseev.userservice.dto;

import ru.patseev.userservice.domain.UserRoles;

public record UserDto (Integer id,
					   String email,
					   String username,
					   String password,
					   String firstName,
					   String lastName,
					   UserRoles role,
					   String activationCode,
					   Boolean enabled) {
}
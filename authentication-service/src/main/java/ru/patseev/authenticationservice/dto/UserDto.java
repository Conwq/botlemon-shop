package ru.patseev.authenticationservice.dto;

import lombok.Builder;

@Builder
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

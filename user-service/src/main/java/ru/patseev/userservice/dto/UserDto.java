package ru.patseev.userservice.dto;

import ru.patseev.userservice.domain.UserRoles;

import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO), представляющий информацию о пользователе для передачи между слоями приложения.
 * Этот класс содержит основные атрибуты пользователя.
 */
public record UserDto (Integer id,
					   String email,
					   String username,
					   String password,
					   String firstName,
					   String lastName,
					   UserRoles role,
					   Timestamp registrationAt,
					   String activationCode,
					   Boolean enabled) {
}
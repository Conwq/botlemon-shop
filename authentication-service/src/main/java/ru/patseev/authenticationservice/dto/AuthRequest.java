package ru.patseev.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank(message = "Username can`no`t be empty") String username,
						  @NotBlank(message = "Password can`t be empty") String password) {
}

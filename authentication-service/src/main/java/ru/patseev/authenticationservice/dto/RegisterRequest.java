package ru.patseev.authenticationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@Email(message = "Incorrect email") String email,
							  @NotBlank(message = "Username can`no`t be empty") String username,
							  @NotBlank(message = "Password can`t be empty") String password,
							  @NotBlank(message = "First name can`t be empty") String firstName,
							  @NotBlank(message = "Second name can`t be empty") String lastName) {
}

package ru.patseev.authenticationservice.dto;

public record AuthenticationRequest(String email, String username, String password, String firstName, String lastName) {
}

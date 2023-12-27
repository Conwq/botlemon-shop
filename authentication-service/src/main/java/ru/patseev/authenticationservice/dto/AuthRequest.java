package ru.patseev.authenticationservice.dto;

public record AuthRequest(String email, String username, String password, String firstName, String lastName) {
}

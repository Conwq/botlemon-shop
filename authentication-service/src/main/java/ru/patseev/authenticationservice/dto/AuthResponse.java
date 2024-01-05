package ru.patseev.authenticationservice.dto;

/**
 * DTO (Data Transfer Object) для ответа на аутентификацию.
 */
public record AuthResponse(String token) {
}
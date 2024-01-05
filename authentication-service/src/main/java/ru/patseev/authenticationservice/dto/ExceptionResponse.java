package ru.patseev.authenticationservice.dto;

import org.springframework.http.HttpStatus;

/**
 * DTO (Data Transfer Object) для представления информации об исключении.
 */
public record ExceptionResponse(String message, HttpStatus status, String createdAt) {
}
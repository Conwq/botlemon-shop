package ru.patseev.itemservice.dto;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(HttpStatus status, String message, String createdAt) {
}

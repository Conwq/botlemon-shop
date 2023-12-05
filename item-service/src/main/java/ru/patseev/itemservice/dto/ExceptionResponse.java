package ru.patseev.itemservice.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(
		HttpStatus status,
		String message,
		String createdAt
) {
}

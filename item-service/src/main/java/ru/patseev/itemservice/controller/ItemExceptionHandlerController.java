package ru.patseev.itemservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.itemservice.dto.ExceptionResponse;
import ru.patseev.itemservice.exception.ItemNotFoundException;

import java.time.Instant;

@ControllerAdvice
public class ItemExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemNotFoundExceptionHandler(ItemNotFoundException e) {
		return createExceptionResponse(e);
	}

	private ResponseEntity<ExceptionResponse> createExceptionResponse(RuntimeException e) {

		HttpStatus notFound = HttpStatus.NOT_FOUND;

		ExceptionResponse response = ExceptionResponse.builder()
				.status(notFound)
				.message(e.getMessage())
				.createdAt(Instant.now().toString())
				.build();

		return new ResponseEntity<>(response, notFound);
	}
}
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
		ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage(), Instant.now().toString());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
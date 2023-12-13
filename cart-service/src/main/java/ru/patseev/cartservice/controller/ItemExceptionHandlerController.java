package ru.patseev.cartservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.cartservice.dto.ExceptionResponse;
import ru.patseev.cartservice.exception.ItemNotFoundException;
import ru.patseev.cartservice.exception.UnacceptableQualityItemsException;
import ru.patseev.cartservice.exception.UserNotFoundException;

import java.time.Instant;

@ControllerAdvice
public class ItemExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemBotFoundExceptionHandler(ItemNotFoundException e) {
		return createExceptionResponse(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> userNotFoundExceptionHandler(UserNotFoundException e) {
		return createExceptionResponse(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnacceptableQualityItemsException.class)
	public ResponseEntity<ExceptionResponse> unacceptableQualityItemsExceptionHandler(UnacceptableQualityItemsException e) {
		return createExceptionResponse(e, HttpStatus.EXPECTATION_FAILED);
	}

	private ResponseEntity<ExceptionResponse> createExceptionResponse(RuntimeException e, HttpStatus status) {

		ExceptionResponse response = ExceptionResponse.builder()
				.status(status)
				.message(e.getMessage())
				.createdAt(Instant.now().toString())
				.build();

		return new ResponseEntity<>(response, status);
	}
}
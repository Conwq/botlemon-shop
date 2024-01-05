package ru.patseev.cartservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.cartservice.dto.ExceptionResponse;
import ru.patseev.cartservice.exception.ItemNotFoundException;
import ru.patseev.cartservice.exception.UnacceptableQualityItemsException;
import ru.patseev.cartservice.exception.UserNotFoundException;

import java.time.Instant;

@ControllerAdvice
public class CartExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemBotFoundExceptionHandler(ItemNotFoundException e) {
		return createExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> userNotFoundExceptionHandler(UserNotFoundException e) {
		return createExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnacceptableQualityItemsException.class)
	public ResponseEntity<ExceptionResponse> unacceptableQualityItemsExceptionHandler(UnacceptableQualityItemsException e) {
		return createExceptionResponse(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException() {
		return createExceptionResponse("Invalid data", HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ExceptionResponse> createExceptionResponse(String message, HttpStatus status) {
		ExceptionResponse response = new ExceptionResponse(status, message, Instant.now().toString());
		return new ResponseEntity<>(response, status);
	}
}
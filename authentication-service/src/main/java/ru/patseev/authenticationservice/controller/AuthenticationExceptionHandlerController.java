package ru.patseev.authenticationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.authenticationservice.dto.ExceptionResponse;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;

import java.time.Instant;

@ControllerAdvice
public class AuthenticationExceptionHandlerController {

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> userAlreadyExistExceptionHandler(UserAlreadyExistException e) {
		return createResponse(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException() {
		return createResponse("Invalid data", HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ExceptionResponse> createResponse(String message, HttpStatus status) {
		ExceptionResponse response = new ExceptionResponse(message, status, Instant.now().toString());
		return new ResponseEntity<>(response, status);
	}
}
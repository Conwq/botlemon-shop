package ru.patseev.usersservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.usersservice.dto.ExceptionResponse;
import ru.patseev.usersservice.exception.IncorrectInputDataException;
import ru.patseev.usersservice.exception.RoleNotFoundException;
import ru.patseev.usersservice.exception.UserAlreadyExistException;
import ru.patseev.usersservice.exception.UserNotFoundException;

import java.time.Instant;

@ControllerAdvice
public class UserExceptionHandlerController {

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> userAlreadyExistExceptionHandler(UserAlreadyExistException e) {
		return this.createExceptionResponse(e, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(IncorrectInputDataException .class)
	public ResponseEntity<?> incorrectInputDataExceptionHandler(IncorrectInputDataException e) {
		return this.createExceptionResponse(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RoleNotFoundException .class)
	public ResponseEntity<?> roleNotFoundExceptionHandler(RoleNotFoundException e) {
		return this.createExceptionResponse(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException .class)
	public ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException e) {
		return this.createExceptionResponse(e, HttpStatus.NOT_FOUND);
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
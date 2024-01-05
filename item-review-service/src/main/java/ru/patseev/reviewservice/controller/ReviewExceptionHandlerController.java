package ru.patseev.reviewservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.reviewservice.dto.ExceptionResponse;
import ru.patseev.reviewservice.exception.*;

import java.time.Instant;

@ControllerAdvice
public class ReviewExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemNotFoundExceptionHandler(ItemNotFoundException e) {
		return this.createExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> userNotFoundExceptionHandler(UserNotFoundException e) {
		return this.createExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ReviewNotFoundException.class)
	public ResponseEntity<ExceptionResponse> reviewNotFoundExceptionHandler(ReviewNotFoundException e) {
		return this.createExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserMismatchException.class)
	public ResponseEntity<ExceptionResponse> userMismatchExceptionHandler(UserMismatchException e) {
		return this.createExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateReviewException.class)
	public ResponseEntity<ExceptionResponse> duplicateReviewException(DuplicateReviewException e) {
		return this.createExceptionResponse(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException() {
		return this.createExceptionResponse("Invalid data", HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ExceptionResponse> createExceptionResponse(String message, HttpStatus status) {
		ExceptionResponse response = new ExceptionResponse(status, message, Instant.now().toString());
		return new ResponseEntity<>(response, status);
	}
}
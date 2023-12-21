package ru.patseev.reviewservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.reviewservice.dto.ExceptionResponse;
import ru.patseev.reviewservice.exception.*;

import java.time.Instant;

@ControllerAdvice
public class ReviewExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemNotFoundExceptionHandler(ItemNotFoundException e) {
		return this.createExceptionResponse(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> userNotFoundExceptionHandler(UserNotFoundException e) {
		return this.createExceptionResponse(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ReviewNotFoundException.class)
	public ResponseEntity<ExceptionResponse> reviewNotFoundExceptionHandler(ReviewNotFoundException e) {
		return this.createExceptionResponse(e, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserMismatchException.class)
	public ResponseEntity<ExceptionResponse> userMismatchExceptionHandler(UserMismatchException e) {
		return this.createExceptionResponse(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateReviewException.class)
	public ResponseEntity<ExceptionResponse> duplicateReviewException(DuplicateReviewException e) {
		return this.createExceptionResponse(e, HttpStatus.CONFLICT);
	}

	private ResponseEntity<ExceptionResponse> createExceptionResponse(RuntimeException e, HttpStatus status) {
		ExceptionResponse response = new ExceptionResponse(status, e.getMessage(), Instant.now().toString());
		return new ResponseEntity<>(response, status);
	}
}
package ru.patseev.itemservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.itemservice.dto.ExceptionResponse;
import ru.patseev.itemservice.exception.ItemNotFoundException;

import java.time.Instant;

@ControllerAdvice
public class ItemExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemNotFoundExceptionHandler(ItemNotFoundException e) {
		return createExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
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
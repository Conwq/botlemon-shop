package ru.patseev.itemservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.itemservice.dto.ExceptionResponse;

import java.time.Instant;

@ControllerAdvice
public class ItemExceptionHandlerController {

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ExceptionResponse> itemBotFoundExceptionHandler(ItemNotFoundException e) {
		return createExceptionResponse(e, HttpStatus.NOT_FOUND);
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
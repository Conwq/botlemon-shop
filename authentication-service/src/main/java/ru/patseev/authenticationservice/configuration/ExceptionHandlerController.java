package ru.patseev.authenticationservice.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.authenticationservice.dto.ExceptionResponse;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> userAlreadyExistExceptionHandler(UserAlreadyExistException e) {
		return new ResponseEntity<>(new ExceptionResponse("Current user exist"), HttpStatus.CONFLICT);
	}
}

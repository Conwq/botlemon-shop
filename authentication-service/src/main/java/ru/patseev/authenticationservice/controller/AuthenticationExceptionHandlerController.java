package ru.patseev.authenticationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.patseev.authenticationservice.dto.ExceptionResponse;
import ru.patseev.authenticationservice.exception.UserAlreadyExistException;

import java.time.Instant;

/**
 * Обработчик исключений для контроллера аутентификации.
 */
@ControllerAdvice
public class AuthenticationExceptionHandlerController {

	/**
	 * Обработчик исключения, возникающего при попытке создания пользователя, который уже существует.
	 *
	 * @param e Исключение UserAlreadyExistException.
	 * @return Ответ с информацией об ошибке и HTTP статусом 409 (CONFLICT).
	 */
	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> userAlreadyExistExceptionHandler(UserAlreadyExistException e) {
		return createResponse(e.getMessage(), HttpStatus.CONFLICT);
	}

	/**
	 * Обработчик исключения, возникающего при невалидных аргументах метода.
	 *
	 * @return Ответ с информацией об ошибке и HTTP статусом 400 (BAD REQUEST).
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException() {
		return createResponse("Invalid data", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Обработчик исключения, возникающего при отсутствии пользователя с указанным именем пользователя.
	 *
	 * @param e Исключение UsernameNotFoundException.
	 * @return Ответ с информацией об ошибке и HTTP статусом 400 (BAD REQUEST).
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ExceptionResponse> usernameNotFoundException(UsernameNotFoundException e) {
		return createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Создает и возвращает объект ExceptionResponse с заданными параметрами.
	 *
	 * @param message Сообщение об ошибке.
	 * @param status  HTTP статус ответа.
	 * @return Объект ExceptionResponse.
	 */
	private ResponseEntity<ExceptionResponse> createResponse(String message, HttpStatus status) {
		ExceptionResponse response = new ExceptionResponse(message, status, Instant.now().toString());
		return new ResponseEntity<>(response, status);
	}
}
package ru.patseev.authenticationservice.exception;

/**
 * Исключение, выбрасываемое при попытке создания пользователя, который уже существует.
 */
public class UserAlreadyExistException extends RuntimeException {

	/**
	 * Конструктор с сообщением об ошибке.
	 *
	 * @param message Сообщение об ошибке.
	 */
	public UserAlreadyExistException(String message) {
		super(message);
	}
}
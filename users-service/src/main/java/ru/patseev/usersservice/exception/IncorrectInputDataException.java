package ru.patseev.usersservice.exception;

public class IncorrectInputDataException extends RuntimeException {

	public IncorrectInputDataException(String message) {
		super(message);
	}
}

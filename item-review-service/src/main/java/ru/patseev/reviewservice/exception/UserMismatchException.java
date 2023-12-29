package ru.patseev.reviewservice.exception;

public class UserMismatchException extends RuntimeException {

	public UserMismatchException(String message) {
		super(message);
	}
}
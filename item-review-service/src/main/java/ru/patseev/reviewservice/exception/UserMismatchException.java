package ru.patseev.reviewservice.exception;

public class UserMismatchException extends RuntimeException {

	public UserMismatchException() {
		super("Users don't match");
	}
}

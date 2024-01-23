package ru.patseev.orderservice.exception;

public class EmptyUsersCartException extends RuntimeException{

	public EmptyUsersCartException(String message) {
		super(message);
	}
}
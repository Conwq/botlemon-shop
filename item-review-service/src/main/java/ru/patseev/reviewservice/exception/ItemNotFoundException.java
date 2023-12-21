package ru.patseev.reviewservice.exception;

public class ItemNotFoundException extends RuntimeException {

	public ItemNotFoundException() {
		super("Item not found");
	}
}

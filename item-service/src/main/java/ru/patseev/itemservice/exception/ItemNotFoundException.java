package ru.patseev.itemservice.exception;

public class ItemNotFoundException extends RuntimeException {

	public ItemNotFoundException() {
		super("Item not found");
	}
}

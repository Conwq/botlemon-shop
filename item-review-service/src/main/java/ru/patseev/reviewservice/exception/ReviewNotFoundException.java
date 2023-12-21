package ru.patseev.reviewservice.exception;

public class ReviewNotFoundException extends RuntimeException {

	public ReviewNotFoundException() {
		super("Review not found");
	}
}

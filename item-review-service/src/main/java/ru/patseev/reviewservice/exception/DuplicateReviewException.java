package ru.patseev.reviewservice.exception;

public class DuplicateReviewException extends RuntimeException {

	public DuplicateReviewException(String message) {
		super(message);
	}
}

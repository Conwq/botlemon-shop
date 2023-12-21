package ru.patseev.reviewservice.dto;

import java.math.BigDecimal;

public record ReviewResponse(Integer reviewId, String firstName, String lastName, String review, BigDecimal rating) {
}

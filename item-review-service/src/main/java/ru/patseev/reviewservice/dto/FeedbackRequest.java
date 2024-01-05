package ru.patseev.reviewservice.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record FeedbackRequest(Integer id,
							  @Size(min = 2, max = 100) String review,
							  @PositiveOrZero BigDecimal rating) {
}

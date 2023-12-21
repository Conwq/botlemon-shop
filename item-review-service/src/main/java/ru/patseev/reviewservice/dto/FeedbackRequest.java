package ru.patseev.reviewservice.dto;

import java.math.BigDecimal;

public record FeedbackRequest(Integer id, String review, BigDecimal rating) {
}

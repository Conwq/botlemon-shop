package ru.patseev.cartservice.dto;

import jakarta.validation.constraints.Positive;

public record CartRequest(int itemId,
						  @Positive
						  int quantity) {
}

package ru.patseev.itemservice.dto;

import java.math.BigDecimal;

public record AccountDto(String email,
						 String username,
						 String firstName,
						 String lastName,
						 String registrationAt,
						 String lastLoginDate,
						 BigDecimal totalPurchaseAmount,
						 Integer discountPercentage) {
}

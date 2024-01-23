package ru.patseev.orderservice.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record ItemDto(Integer id,
					  String name,
					  String description,
					  BigDecimal price,
					  Timestamp publicationDate,
					  Integer quantity,
					  BigDecimal totalPrice) {
}

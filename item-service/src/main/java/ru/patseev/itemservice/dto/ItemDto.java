package ru.patseev.itemservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
	private Integer id;
	@NotBlank(message = "Name cannot be empty")
	private String name;
	private String description;
	@PositiveOrZero
	private BigDecimal price;
	@Builder.Default
	private Timestamp publicationDate = Timestamp.from(Instant.now());
	@PositiveOrZero
	private Integer quantity;
	private BigDecimal discountAmount;
	private BigDecimal discountedPrice;
}
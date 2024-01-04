package ru.patseev.itemservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
	@Min(value = 0)
	private BigDecimal price;
	@Builder.Default
	private Timestamp publicationDate = Timestamp.from(Instant.now());
	@Min(value = 0)
	private Integer quantity;
}
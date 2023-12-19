package ru.patseev.itemservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

	private Integer id;

	private String name;

	private String description;

	private BigDecimal price;

	private BigDecimal rating;

	private Integer voters;

	private Timestamp publicationDate;

	private Integer quantity;
}
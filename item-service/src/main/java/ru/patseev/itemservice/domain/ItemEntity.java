package ru.patseev.itemservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(schema = "botlemon", name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private BigDecimal price;
	private Timestamp publicationDate;

	@PrePersist
	public void saveDescription() {
		if (description == null || description.trim().isEmpty()) {
			description = "No description";
		}
	}
}
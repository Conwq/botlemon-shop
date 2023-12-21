package ru.patseev.cartservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
	@OneToMany(mappedBy = "itemEntity",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<CartEntity> cartEntities;
	public List<CartEntity> getCartEntities() {
		if (cartEntities == null) {
			return new ArrayList<>();
		}

		return cartEntities;
	}
}
package ru.patseev.orderservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "botlemon", name = "orders_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
	@Id
	@Column(name = "orders_items_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderItemId;
	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderEntity order;
	private Integer itemId;
	private Integer quantity;
}
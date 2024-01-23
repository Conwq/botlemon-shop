package ru.patseev.orderservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(schema = "botlemon", name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;
	private Integer userId;
	private Timestamp orderDate;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderItemEntity> orders;

	/**
	 * Перед сохранением устанавливает дату на текущую, если значение == null.
	 */
	@PrePersist
	public void setOrderDate() {
		if (orderDate == null) {
			orderDate = Timestamp.from(Instant.now());
		}
	}
}
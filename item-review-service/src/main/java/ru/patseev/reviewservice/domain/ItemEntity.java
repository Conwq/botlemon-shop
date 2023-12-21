package ru.patseev.reviewservice.domain;

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
	@OneToMany(mappedBy = "itemEntity", fetch = FetchType.LAZY)
	private List<ReviewEntity> reviewEntityList;

	public void addReviewEntity(ReviewEntity reviewEntity) {
		if (reviewEntityList == null) {
			reviewEntityList = new ArrayList<>();
		}
		reviewEntityList.add(reviewEntity);
	}
}
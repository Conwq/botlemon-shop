package ru.patseev.reviewservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "botlemon", name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private Timestamp registrationAt;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private RoleEntity roleEntity;
	@OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY)
	private List<ReviewEntity> reviewEntityList;

	public void addReviewEntity(ReviewEntity reviewEntity) {
		if (reviewEntityList == null) {
			reviewEntityList = new ArrayList<>();
		}
		reviewEntityList.add(reviewEntity);
	}
}
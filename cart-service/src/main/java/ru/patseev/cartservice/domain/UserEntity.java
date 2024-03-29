package ru.patseev.cartservice.domain;

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

	@OneToMany(mappedBy = "userEntity",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<CartEntity> cartEntities;

	@Override
	public String toString() {
		return String.format("User: id -> %s, user -> %s", id, username);
	}

	public List<CartEntity> getCartEntities() {
		if (cartEntities == null) {
			return new ArrayList<>();
		}

		return cartEntities;
	}
}
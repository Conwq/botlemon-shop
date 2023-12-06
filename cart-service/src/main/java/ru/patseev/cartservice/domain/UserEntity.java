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

	@ManyToMany
	@JoinTable(
			name = "carts",
			schema = "botlemon",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "item_id")
	)
	private List<ItemEntity> itemList;

	public void addItem(ItemEntity itemEntity) {
		if (itemList == null) {
			itemList = new ArrayList<>();
		}

		itemList.add(itemEntity);
	}

	public void removeItem(ItemEntity itemEntity) {
		itemList.remove(itemEntity);
	}

	@Override
	public String toString() {
		return String.format("User: id -> %s, user -> %s", id, username);
	}
}
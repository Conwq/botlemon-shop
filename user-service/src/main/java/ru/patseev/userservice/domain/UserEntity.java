package ru.patseev.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

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
	private String email;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private Timestamp registrationAt;
	private String activationCode;
	@ManyToOne
	@JoinColumn(name = "role_id")
	private RoleEntity role;

	/**
	 * Устанавливает дату регистрации перед сохранением в базу данных.
	 */
	@PrePersist
	public void setRegistrationDate() {
		if (registrationAt == null) {
			registrationAt = Timestamp.from(Instant.now());
		}
	}
}
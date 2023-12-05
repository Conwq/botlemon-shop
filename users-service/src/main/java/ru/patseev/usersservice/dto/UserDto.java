package ru.patseev.usersservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private Integer id;

	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String firstName;

	private String lastName;

	private boolean enabled;

	private Timestamp registrationAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UserRoles userRole;

	@ToString.Include(name = "password")
	public String maskPassword() {
		return "********";
	}
}

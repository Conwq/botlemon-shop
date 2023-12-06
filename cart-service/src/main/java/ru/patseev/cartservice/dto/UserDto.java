package ru.patseev.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.patseev.cartservice.domain.ItemEntity;
import ru.patseev.cartservice.domain.RoleEntity;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private Integer id;

	private String username;

	private String password;

	private String firstName;

	private String lastName;

	private boolean enabled;

	private Timestamp registrationAt;

	private RoleEntity roleEntity;

	private List<ItemEntity> itemList;
}

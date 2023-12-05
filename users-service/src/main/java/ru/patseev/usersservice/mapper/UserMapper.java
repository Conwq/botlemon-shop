package ru.patseev.usersservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.patseev.usersservice.domain.UserEntity;
import ru.patseev.usersservice.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mappings({
			@Mapping(target = "password", ignore = true),
			@Mapping(target = "userRole", source = "roleEntity.roleName")
	})
	UserDto toDto(UserEntity userEntity);

	@Mappings({
			@Mapping(target = "password"),
			@Mapping(target = "roleEntity", source = "userRole")
	})
	UserEntity toEntity(UserDto userDto);
}
package ru.patseev.cartservice.mapper;

import org.mapstruct.Mapper;
import ru.patseev.cartservice.domain.ItemEntity;
import ru.patseev.cartservice.dto.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {
	ItemEntity toEntity(ItemDto itemDto);
	ItemDto toDto(ItemEntity itemEntity);
}

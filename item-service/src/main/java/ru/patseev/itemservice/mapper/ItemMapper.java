package ru.patseev.itemservice.mapper;

import org.mapstruct.Mapper;
import ru.patseev.itemservice.domain.ItemEntity;
import ru.patseev.itemservice.dto.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

	ItemDto toDto(ItemEntity itemEntity);

	ItemEntity toEntity(ItemDto itemDto);
}

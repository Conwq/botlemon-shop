package ru.patseev.itemservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.itemservice.domain.ItemEntity;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.exception.ItemNotFoundException;
import ru.patseev.itemservice.mapper.ItemMapper;
import ru.patseev.itemservice.repository.ItemRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final ItemMapper itemMapper;

	@Transactional(readOnly = true)
	public ItemDto getItemById(int id) {
		return itemRepository
				.findById(id)
				.map(itemMapper::toDto)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));
	}

	@Transactional(readOnly = true)
	public List<ItemDto> getAllItems() {
		return itemRepository
				.findAll()
				.stream().map(itemMapper::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public ItemDto addItem(ItemDto itemDto) {
		ItemEntity itemEntity = itemMapper.toEntity(itemDto);
		itemEntity.setPublicationDate(Timestamp.from(Instant.now()));
		itemEntity.setRating(new BigDecimal("0.0"));
		itemEntity.setVoters(0);

		return itemMapper.toDto(itemRepository.save(itemEntity));
	}

	@Transactional
	public ItemDto editItem(ItemDto itemDto) {
		ItemEntity item = itemRepository
				.findById(itemDto.getId())
				.map(itemEntity -> this.updateItemField(itemDto, itemEntity))
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));

		return itemMapper.toDto(item);
	}

	private ItemEntity updateItemField(ItemDto itemDto, ItemEntity itemEntity) {
		if (Objects.nonNull(itemDto.getName())) itemEntity.setName(itemDto.getName());
		if (Objects.nonNull(itemDto.getDescription())) itemEntity.setDescription(itemDto.getDescription());
		if (Objects.nonNull(itemDto.getPrice())) itemEntity.setPrice(itemDto.getPrice());
		if (Objects.nonNull(itemDto.getCount())) itemEntity.setCount(itemDto.getCount());

		return itemEntity;
	}
}
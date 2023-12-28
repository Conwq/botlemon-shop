package ru.patseev.itemservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.itemservice.client.StorageServiceClient;
import ru.patseev.itemservice.domain.ItemEntity;
import ru.patseev.itemservice.dto.Actions;
import ru.patseev.itemservice.dto.InfoResponse;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.exception.ItemNotFoundException;
import ru.patseev.itemservice.mapper.ItemMapper;
import ru.patseev.itemservice.repository.ItemRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemMapper itemMapper;
	private final StorageServiceClient storageServiceClient;

	@Transactional(readOnly = true)
	public ItemDto getItemById(int itemId) {
		return itemRepository
				.findById(itemId)
				.map(this::mapItemEntityToItemDtoWithQuantity)
				.orElseThrow(ItemNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public List<ItemDto> getAllItems() {
		return itemRepository.findAll()
				.stream()
				.map(this::mapItemEntityToItemDtoWithQuantity)
				.toList();
	}

	//TODO transactional
	public ResponseEntity<InfoResponse> addItem(ItemDto itemDto) {
		ItemEntity itemEntity = this.createItemEntity(itemDto);
		int itemId = itemRepository.save(itemEntity).getId();

		storageServiceClient.saveQuantityItemToStorage(itemId, itemDto.getQuantity());

		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	@Transactional
	public ResponseEntity<InfoResponse> deleteItem(int itemId) {
		itemRepository.findById(itemId)
				.orElseThrow(ItemNotFoundException::new);

		storageServiceClient.deleteQuantityItemFromStorage(itemId);
		itemRepository.deleteById(itemId);

		return createResponse(Actions.DELETE, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<InfoResponse> editItem(ItemDto itemDto) {
		itemRepository
				.findById(itemDto.getId())
				.map(itemEntity -> {
					this.updateItemField(itemDto, itemEntity);
					return itemRepository.save(itemEntity);
				})
				.orElseThrow(ItemNotFoundException::new);

		return createResponse(Actions.EDIT, HttpStatus.OK);
	}

	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}

	private void updateItemField(ItemDto itemDto, ItemEntity itemEntity) {
		final String name;
		final String description;
		final BigDecimal price;

		if (Objects.nonNull(name = itemDto.getName())) {
			itemEntity.setName(name);
		}
		if (Objects.nonNull(description = itemDto.getDescription())) {
			itemEntity.setDescription(description);
		}
		if (Objects.nonNull(price = itemDto.getPrice())) {
			itemEntity.setPrice(price);
		}
		if (Objects.nonNull(itemDto.getQuantity())) {
			storageServiceClient.updateStorageQuantity(itemDto);
		}
	}

	private ItemDto mapItemEntityToItemDtoWithQuantity(ItemEntity itemEntity) {
		int quantityItem = storageServiceClient.getQuantityItem(itemEntity.getId());
		ItemDto itemDto = itemMapper.toDto(itemEntity);
		itemDto.setQuantity(quantityItem);
		return itemDto;
	}

	private ItemEntity createItemEntity(ItemDto itemDto) {
		ItemEntity itemEntity = itemMapper.toEntity(itemDto);
		itemEntity.setPublicationDate(Timestamp.from(Instant.now()));
		return itemEntity;
	}
}
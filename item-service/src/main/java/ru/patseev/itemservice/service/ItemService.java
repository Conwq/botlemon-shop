package ru.patseev.itemservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final ItemMapper itemMapper;

	private final WebClient.Builder webClientBuilder;

	@Transactional(readOnly = true)
	public ItemDto getItemById(int itemId) {
		return itemRepository
				.findById(itemId)
				.map(this::getItemWithQuantity)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));
	}

	@Transactional(readOnly = true)
	public List<ItemDto> getAllItems() {
		return itemRepository
				.findAll()
				.stream()
				.map(this::getItemWithQuantity)
				.collect(Collectors.toList());
	}

	public ResponseEntity<InfoResponse> addItem(ItemDto itemDto) {
		ItemEntity itemEntity = this.createItemEntity(itemDto);

		int itemId = itemRepository.save(itemEntity).getId();

		this.saveQuantityItemToStorage(itemId, itemDto.getQuantity());

		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	@Transactional
	public ResponseEntity<InfoResponse> editItem(ItemDto itemDto) {
		itemRepository
				.findById(itemDto.getId())
				.map(itemEntity -> {
					this.updateItemField(itemDto, itemEntity);
					return itemRepository.save(itemEntity);
				})
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));

		return createResponse(Actions.EDIT, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<InfoResponse> deleteItem(int itemId) {
		if (!itemRepository.existsById(itemId)) {
			throw new ItemNotFoundException("Item not found");
		}

		this.deleteQuantityItemFromStorage(itemId);
		itemRepository.deleteById(itemId);
		
		return createResponse(Actions.DELETE, HttpStatus.OK);
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
			this.updateStorageQuantity(itemDto);
		}
	}

	private void updateStorageQuantity(ItemDto itemDto) {
		webClientBuilder
				.build()
				.put()
				.uri("http://storage-service/v1/api/storage/{itemId}/{quantity}",
						uriBuilder -> uriBuilder.build(itemDto.getId(), itemDto.getQuantity()))
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	private ItemDto getItemWithQuantity(ItemEntity itemEntity) {
		Integer quantity = webClientBuilder
				.build()
				.get()
				.uri("http://storage-service/v1/api/storage/{itemId}",
						uriBuilder -> uriBuilder.build(itemEntity.getId()))
				.retrieve()
				.bodyToMono(Integer.class)
				.block();

		ItemDto itemDto = itemMapper.toDto(itemEntity);
		itemDto.setQuantity(quantity);

		return itemDto;
	}

	private ItemEntity createItemEntity(ItemDto itemDto) {
		ItemEntity itemEntity = itemMapper.toEntity(itemDto);
		itemEntity.setPublicationDate(Timestamp.from(Instant.now()));
		itemEntity.setRating(new BigDecimal("0.0"));
		itemEntity.setVoters(0);
		return itemEntity;
	}

	private void saveQuantityItemToStorage(int itemId, int itemQuantity) {
		webClientBuilder
				.build()
				.post()
				.uri("http://storage-service/v1/api/storage/{itemId}/{quantity}",
						uriBuilder -> uriBuilder.build(itemId, itemQuantity))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
	
	private void deleteQuantityItemFromStorage(int itemId) {
		webClientBuilder
				.build()
				.delete()
				.uri("http://storage-service/v1/api/storage/{itemId}",
						uriBuilder -> uriBuilder.build(itemId))
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
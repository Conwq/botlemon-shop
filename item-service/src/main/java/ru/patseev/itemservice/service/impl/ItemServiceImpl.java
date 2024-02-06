package ru.patseev.itemservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.itemservice.client.AccountServiceClient;
import ru.patseev.itemservice.client.StorageServiceClient;
import ru.patseev.itemservice.domain.ItemEntity;
import ru.patseev.itemservice.dto.AccountDto;
import ru.patseev.itemservice.dto.Actions;
import ru.patseev.itemservice.dto.InfoResponse;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.exception.ItemNotFoundException;
import ru.patseev.itemservice.mapper.ItemMapper;
import ru.patseev.itemservice.repository.ItemRepository;
import ru.patseev.itemservice.service.ItemService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;
	private final ItemMapper itemMapper;
	private final StorageServiceClient storageServiceClient;
	private final AccountServiceClient accountServiceClient;

	@Override
	@Transactional(readOnly = true)
	public ItemDto getItem(int itemId,
						   @Nullable String header) {
		return itemRepository
				.findById(itemId)
				.map(item -> this.toDto(item, header))
				.orElseThrow(ItemNotFoundException::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDto> getAllItems(@Nullable String header) {
		return itemRepository.findAll()
				.stream()
				.map(item -> this.toDto(item, header))
				.toList();
	}

	//TODO transactional
	@Override
	public ResponseEntity<InfoResponse> addItem(ItemDto itemDto) {
		ItemEntity itemEntity = itemMapper.toEntity(itemDto);
		int itemId = itemRepository.save(itemEntity).getId();

		storageServiceClient.saveQuantityItemToStorage(itemId, itemDto.getQuantity());

		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	@Override
	@Transactional
	public ResponseEntity<InfoResponse> deleteItem(int itemId) {
		itemRepository.findById(itemId)
				.orElseThrow(ItemNotFoundException::new);

		storageServiceClient.deleteQuantityItemFromStorage(itemId);
		itemRepository.deleteById(itemId);

		return createResponse(Actions.DELETE, HttpStatus.OK);
	}

	@Override
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

	/*
	 * Создает тело ответа со статусом.
	 */
	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}

	/*
	 * Обновляет поля предмета.
	 */
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
			storageServiceClient.updateItemQuantityInStorage(itemDto);
		}
	}

	/*
	 * Преобразует сущность ItemEntity в объект DTO ItemDto с учетом количества товара и скидки.
	 */
	private ItemDto toDto(ItemEntity entity,
						  @Nullable String header) {
		int quantityItem = storageServiceClient.getQuantityItemFromStorage(entity.getId());
		int discountPercentage = getDiscountOnProduct(header);
		BigDecimal itemPrice = entity.getPrice();

		ItemDto.ItemDtoBuilder builder = ItemDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.price(entity.getPrice())
				.publicationDate(entity.getPublicationDate())
				.quantity(quantityItem);

		if (discountPercentage != 0) {
			BigDecimal discountAmount = itemPrice.multiply(BigDecimal.valueOf(discountPercentage / 100.0));

			builder.discountAmount(
					discountAmount.setScale(2, RoundingMode.HALF_UP));

			builder.discountedPrice(
					itemPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP));
		}
		return builder.build();
	}

	private int getDiscountOnProduct(String header) {
		if (header != null) {
			AccountDto accountDto = accountServiceClient.sendRequestToReceiveAccountDetails(header);
			return accountDto.discountPercentage();
		}
		return 0;
	}
}
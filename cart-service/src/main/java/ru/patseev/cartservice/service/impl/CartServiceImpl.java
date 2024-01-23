package ru.patseev.cartservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.cartservice.client.StorageServiceClient;
import ru.patseev.cartservice.domain.CartEntity;
import ru.patseev.cartservice.domain.ItemEntity;
import ru.patseev.cartservice.domain.UserEntity;
import ru.patseev.cartservice.dto.Actions;
import ru.patseev.cartservice.dto.CartRequest;
import ru.patseev.cartservice.dto.InfoResponse;
import ru.patseev.cartservice.dto.ItemDto;
import ru.patseev.cartservice.exception.ItemNotFoundException;
import ru.patseev.cartservice.exception.UnacceptableQualityItemsException;
import ru.patseev.cartservice.exception.UserNotFoundException;
import ru.patseev.cartservice.mapper.ItemMapper;
import ru.patseev.cartservice.repository.CartRepository;
import ru.patseev.cartservice.repository.ItemRepository;
import ru.patseev.cartservice.repository.UserRepository;
import ru.patseev.cartservice.service.CartService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;
	private final ItemMapper itemMapper;
	private final StorageServiceClient storageServiceClient;

	@Override
	@Transactional(readOnly = true)
	public List<ItemDto> getUsersShoppingCart(int userId) {
		return cartRepository
				.findAllByUserEntityId(userId)
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ResponseEntity<InfoResponse> addItemToUsersShoppingCart(int userId, CartRequest request) {
		final int itemId = request.itemId();
		int quantity = request.quantity();
		if (quantity <= 0) {
			return createResponse(Actions.ADD, HttpStatus.BAD_REQUEST);
		}

		if (!this.isEnoughItemForStorage(itemId, quantity)) {
			throw new UnacceptableQualityItemsException("Unacceptable quality of items");
		}

		CartEntity cartEntity = this.findOrCreateCartEntity(userId, itemId, quantity);

		ResponseEntity<Object> response = storageServiceClient.addItemQuantityToCart(itemId, quantity);

		if (!response.getStatusCode().is2xxSuccessful()) {
			return createResponse(Actions.ADD, HttpStatus.BAD_REQUEST);
		}

		cartRepository.save(cartEntity);
		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	@Override
	@Transactional
	public ResponseEntity<InfoResponse> removeItemFromUsersShoppingCart(int userId, CartRequest request) {
		int quantity;
		CartEntity cartEntity = this.getCartEntityByUserIdAndItemId(userId, request.itemId());
		if (cartEntity.getQuantity() <= request.quantity()) {
			quantity = cartEntity.getQuantity();
			cartRepository.delete(cartEntity);
		} else {
			quantity = request.quantity();
			cartEntity.setQuantity(cartEntity.getQuantity() - request.quantity());
		}
		ResponseEntity<Object> response
				= storageServiceClient.returnQuantityOfItemToStorage(request.itemId(), quantity);
		if (!response.getStatusCode().is2xxSuccessful()) {
			return createResponse(Actions.REMOVE, HttpStatus.BAD_REQUEST);
		}
		return createResponse(Actions.REMOVE, HttpStatus.OK);
	}

	@Override
	@Transactional
	public void removeUsersCart(int userId) {
		cartRepository.deleteAllByUserEntityId(userId);
	}

	/*
	 * Получение сущности Cart по userId и itemId, и меняет количество товара, добавляя к старому значение новое.
	 * Если не найдено - то возвращает новую сущность с количеством, которое указал пользователь.
	 */
	private CartEntity findOrCreateCartEntity(int userId, int itemId, int quantity) {
		return cartRepository
				.findByUserEntityIdAndItemEntityId(userId, itemId)
				.map(cart -> {
					int itemQuantity = cart.getQuantity();
					cart.setQuantity(quantity + itemQuantity);
					return cart;
				})
				.orElseGet(() -> this.buildCartEntity(userId, itemId, quantity));
	}

	/*
	 * Создание Dto, для отправки информации пользователю
	 */
	private ItemDto mapToDto(CartEntity cartEntity) {
		ItemEntity itemEntity = cartEntity.getItemEntity();
		ItemDto itemDto = itemMapper.toDto(itemEntity);
		itemDto.setQuantity(cartEntity.getQuantity());
		itemDto.setTotalPrice(itemEntity.getPrice().multiply(BigDecimal.valueOf(cartEntity.getQuantity())));
		return itemDto;
	}

	/*
	 * Получение сущности Item по itemId, если не найдено - то выкидывается ошибка
	 */
	private ItemEntity getItemEntityByItemId(int itemId) {
		return itemRepository
				.findById(itemId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));
	}

	/*
	 * Получение сущности User по userId, если не найдено - то выкидывается ошибка
	 */
	private UserEntity getUserEntityByUserId(int userId) {
		return userRepository
				.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	/*
	 * Получение сущности Cart по userId и itemId, если не найдено - то выкидывается ошибка
	 */
	private CartEntity getCartEntityByUserIdAndItemId(int userId, int itemId) {
		return cartRepository
				.findByUserEntityIdAndItemEntityId(userId, itemId)
				.orElseThrow(() -> new ItemNotFoundException("Cart not found"));
	}

	/*
	 * Создание сущности CartEntity
	 */
	private CartEntity buildCartEntity(int userId, int itemId, int quantity) {
		UserEntity userEntity = this.getUserEntityByUserId(userId);
		ItemEntity itemEntity = this.getItemEntityByItemId(itemId);

		CartEntity cartEntity = CartEntity.builder()
				.userEntity(userEntity)
				.itemEntity(itemEntity)
				.quantity(quantity)
				.build();

		userEntity.getCartEntities().add(cartEntity);
		itemEntity.getCartEntities().add(cartEntity);

		return cartEntity;
	}

	/*
	 * Метод создания ответа с информацией об операции и статус кодом.
	 */
	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}

	/*
	 * Проверяем доступное количество предметов на складе
	 */
	private boolean isEnoughItemForStorage(int itemId, int quantity) {
		int itemQuantity = storageServiceClient.getAvailableItemQuantity(itemId); //Получает количество товара со склада.

		return itemQuantity - quantity >= 0;
	}
}
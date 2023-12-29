package ru.patseev.cartservice.service;

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
import ru.patseev.cartservice.exception.UserNotFoundException;
import ru.patseev.cartservice.mapper.ItemMapper;
import ru.patseev.cartservice.repository.CartRepository;
import ru.patseev.cartservice.repository.ItemRepository;
import ru.patseev.cartservice.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с корзиной пользователя.
 */
@Service
@RequiredArgsConstructor
public class CartService {
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;
	private final ItemMapper itemMapper;
	private final StorageServiceClient storageServiceClient;

	/**
	 * Получает корзину пользователя по его идентификатору.
	 *
	 * @param userId Идентификатор пользователя.
	 * @return Список товаров в корзине пользователя.
	 */
	@Transactional(readOnly = true)
	public List<ItemDto> getUsersShoppingCart(int userId) {
		return cartRepository
				.findAllByUserEntityId(userId)
				.stream()
				.map(this::mapCartEntityToItemDtoWithQuantity)
				.collect(Collectors.toList());
	}

	/**
	 * Добавляет товар в корзину пользователя.
	 *
	 * @param userId  Идентификатор пользователя.
	 * @param request Запрос на добавление товара в корзину.
	 * @return Ответ с информацией об операции и статус кодом.
	 */
	@Transactional
	public ResponseEntity<InfoResponse> addItemToUsersShoppingCart(int userId, CartRequest request) {
		final int itemId = request.itemId();
		int quantity;

		if ((quantity = request.quantity()) <= 0) {
			return createResponse(Actions.ADD, HttpStatus.BAD_REQUEST);
		}

		storageServiceClient.checkAvailableItemQuantity(itemId, quantity);

		CartEntity cartEntity
				= this.findOrCreateCartEntity(userId, itemId, quantity);

		//TODO придумать обработку ответа
		ResponseEntity<Object> response
				= storageServiceClient.addItemQuantityToCart(itemId, quantity);

		cartRepository.save(cartEntity);
		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	/**
	 * Удаляет товар из корзины пользователя.
	 *
	 * @param userId  Идентификатор пользователя.
	 * @param request Запрос на удаление товара из корзины.
	 * @return Ответ с информацией об операции и статус кодом.
	 */
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

		//TODO придумать обработку ответа
		ResponseEntity<Object> response
				= storageServiceClient.returnQuantityOfItemToStorage(request.itemId(), quantity);

		return createResponse(Actions.REMOVE, HttpStatus.OK);
	}

	/*
	 * Получение сущности Cart по userId и itemId. Если не найдено - то возвращает новую сущность.
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
	private ItemDto mapCartEntityToItemDtoWithQuantity(CartEntity cartEntity) {
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
}
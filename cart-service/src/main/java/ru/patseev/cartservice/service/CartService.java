package ru.patseev.cartservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

	private final UserRepository userRepository;

	private final ItemRepository itemRepository;

	private final CartRepository cartRepository;

	private final ItemMapper itemMapper;

	private final RestTemplate restTemplate;

	@Transactional(readOnly = true)
	public List<ItemDto> getUserShoppingCart(int userId) {
		return cartRepository
				.findAllByUserEntityId(userId)
				.stream()
				.map(this::getAndMapItemEntityToDtoWithQuantity)
				.collect(Collectors.toList());
	}

	@Transactional
	public ResponseEntity<InfoResponse> addItemToCart(int userId, CartRequest request) {
		final int itemId = request.itemId();
		final int quantity = request.quantity();

		this.checkAvailableItemQuantity(itemId, quantity);

		CartEntity cartEntity
				= this.getCartEntityFromRepositoryByUserIdAndItemIdOrCreateNewUserIfUserNotExist(userId, itemId, quantity);

		//TODO может отправлять какой нибудь объект для запроса с данными? exchange Например DeleteRequest(int itemId, int quantity)
		restTemplate.delete(
				"http://localhost:5013/v1/api/storage/{itemId}/{quantity}",
				itemId,
				quantity);

		cartRepository.save(cartEntity);

		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	@Transactional
	public ResponseEntity<InfoResponse> removeItemFromCart(int userId, CartRequest request) {
		int requestQuantity = request.quantity();
		int itemId = request.itemId();
		CartEntity cartEntity = this.getCartEntityByUserIdAndItemId(userId, itemId);
		int cartEntityQuantity = cartEntity.getQuantity();

		if (cartEntityQuantity <= requestQuantity) {
			restTemplate.put(
					"http://localhost:5013/v1/api/storage/return/{itemId}/{quantity}",
					null,
					itemId,
					cartEntityQuantity);

			cartRepository.delete(cartEntity);
		} else {
			restTemplate.put(
					"http://localhost:5013/v1/api/storage/return/{itemId}/{quantity}",
					null,
					itemId,
					requestQuantity);

			cartEntity.setQuantity(cartEntityQuantity - requestQuantity);
		}

		return createResponse(Actions.REMOVE, HttpStatus.OK);
	}

	private CartEntity getCartEntityFromRepositoryByUserIdAndItemIdOrCreateNewUserIfUserNotExist(int userId, int itemId, int quantity) {
		return cartRepository
				.findByUserEntityIdAndItemEntityId(userId, itemId)
				.map(cart -> {
					int itemQuantity = cart.getQuantity();
					cart.setQuantity(quantity + itemQuantity);
					return cart;
				})
				.orElseGet(() -> this.buildCartEntity(userId, itemId, quantity));
	}

	private ItemDto getAndMapItemEntityToDtoWithQuantity(CartEntity cartEntity) {
		ItemEntity itemEntity = cartEntity.getItemEntity();
		ItemDto itemDto = itemMapper.toDto(itemEntity);
		itemDto.setQuantity(cartEntity.getQuantity());
		itemDto.setTotalPrice(itemEntity.getPrice().multiply(BigDecimal.valueOf(cartEntity.getQuantity())));
		return itemDto;
	}

	private ItemEntity getItemEntityByItemId(int itemId) {
		return itemRepository
				.findById(itemId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));
	}

	private UserEntity getUserEntityByUserId(int userId) {
		return userRepository
				.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	private CartEntity getCartEntityByUserIdAndItemId(int userId, int itemId) {
		return cartRepository
				.findByUserEntityIdAndItemEntityId(userId, itemId)
				.orElseThrow(() -> new ItemNotFoundException("Cart not found"));
	}

	private CartEntity buildCartEntity(int userId, int itemId, int quantity) {
		UserEntity userEntity = this.getUserEntityByUserId(userId);
		ItemEntity itemEntity = this.getItemEntityByItemId(itemId);
		CartEntity cartEntity = CartEntity
				.builder()
				.userEntity(userEntity)
				.itemEntity(itemEntity)
				.quantity(quantity)
				.build();

		userEntity.getCartEntities().add(cartEntity);
		itemEntity.getCartEntities().add(cartEntity);

		return cartEntity;
	}

	private void checkAvailableItemQuantity(int itemId, int quantity) {
		int itemQuantity = Objects.requireNonNull(restTemplate.getForObject(
				"http://localhost:5013/v1/api/storage/{itemId}",
				Integer.class,
				itemId));

		if (itemQuantity - quantity < 0) {
			throw new UnacceptableQualityItemsException("Unacceptable quality of items");
		}
	}

	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}
}
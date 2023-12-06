package ru.patseev.cartservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.cartservice.domain.ItemEntity;
import ru.patseev.cartservice.domain.UserEntity;
import ru.patseev.cartservice.dto.Actions;
import ru.patseev.cartservice.dto.InfoResponse;
import ru.patseev.cartservice.dto.ItemDto;
import ru.patseev.cartservice.exception.ItemNotFoundException;
import ru.patseev.cartservice.exception.UserNotFoundException;
import ru.patseev.cartservice.mapper.ItemMapper;
import ru.patseev.cartservice.repository.ItemRepository;
import ru.patseev.cartservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

	private final UserRepository userRepository;

	private final ItemRepository itemRepository;

	private final ItemMapper itemMapper;

	@Transactional(readOnly = true)
	public List<ItemDto> getUserShoppingCart(int userId) {

		UserEntity userEntity = userRepository
				.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		return userEntity
				.getItemList()
				.stream()
				.map(itemMapper::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public ResponseEntity<InfoResponse> addItemToCart(int userId, int itemId) {

		UserEntity userEntity = userRepository
				.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		ItemEntity itemEntity = itemRepository
				.findById(itemId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));

		userEntity.addItem(itemEntity);
		itemEntity.addUser(userEntity);

		return createResponse(Actions.ADD, HttpStatus.CREATED);
	}

	@Transactional
	public ResponseEntity<InfoResponse> removeItemFromCart(int userId, int itemId) {

		UserEntity userEntity = userRepository
				.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		ItemEntity itemEntity = itemRepository
				.findById(itemId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));

		userEntity.removeItem(itemEntity);

		return createResponse(Actions.REMOVE, HttpStatus.OK);
	}

	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}
}
package ru.patseev.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.jwtservice.starter.service.JwtHeader;
import ru.patseev.jwtservice.starter.service.JwtService;
import ru.patseev.orderservice.client.CartServiceClient;
import ru.patseev.orderservice.domain.OrderEntity;
import ru.patseev.orderservice.domain.OrderItemEntity;
import ru.patseev.orderservice.dto.ItemDto;
import ru.patseev.orderservice.exception.EmptyUsersCartException;
import ru.patseev.orderservice.repository.OrderItemRepository;
import ru.patseev.orderservice.repository.OrderRepository;
import ru.patseev.orderservice.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final CartServiceClient cartServiceClient;
	private final JwtService jwtService;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	@Override
	@Transactional
	public void placeOrder(String authHeader) {
		List<ItemDto> itemsList = cartServiceClient.sendRequestToReceiveUsersCart(authHeader);

		if (itemsList.isEmpty()) {
			throw new EmptyUsersCartException("Ваша корзина пуста.");
		}
		int userId = extractUserId(authHeader);

		OrderEntity order = OrderEntity.builder()
				.userId(userId).build();

		orderRepository.save(order);

		itemsList.stream()
				.map(dto ->
						OrderItemEntity.builder()
								.order(order)
								.itemId(dto.id())
								.quantity(dto.quantity())
								.build())
				.forEach(orderItemRepository::save);

		cartServiceClient.sendRequestToRemoveItemFromCart(authHeader);
	}

	private int extractUserId(String authHeader) {
		String token = authHeader.replace(JwtHeader.BEARER, "");
		return jwtService.extractUserId(token);
	}
}
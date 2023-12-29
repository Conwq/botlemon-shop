package ru.patseev.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.cartservice.dto.CartRequest;
import ru.patseev.cartservice.dto.InfoResponse;
import ru.patseev.cartservice.dto.ItemDto;
import ru.patseev.cartservice.service.CartService;
import ru.patseev.jwtservice.starter.service.JwtHeader;
import ru.patseev.jwtservice.starter.service.JwtService;

import java.util.List;

/**
 * Контроллер предназначенный для работы с корзиной покупок.
 */
@RestController
@RequestMapping("/v1/api/cart")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final JwtService jwtService;

	/**
	 * Возвращает корзину с покупками пользователя.
	 *
	 * @param header Заголовок Authorization, который содержит в себе JSON Web Token.
	 * @return Список товаров, добавленных пользователем.
	 */
	@GetMapping
	public List<ItemDto> getUserShoppingCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
		final String token = header.replace(JwtHeader.BEARER, "");
		int userId = jwtService.extractUserId(token);

		return cartService.getUsersShoppingCart(userId);
	}

	/**
	 * Добавляет товар в корзину пользователя.
	 *
	 * @param header  Заголовок Authorization, который содержит в себе JSON Web Token.
	 * @param request Запрос с данными на добавление товара и его количества в корзину.
	 * @return Объект InfoResponse с информацией и статусом о добавлении товара в корзину.
	 */
	@PostMapping
	public ResponseEntity<InfoResponse> addItemToCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String header,
													  @RequestBody CartRequest request) {
		String token = header.replace(JwtHeader.BEARER, "");
		int userId = jwtService.extractUserId(token);

		return cartService.addItemToUsersShoppingCart(userId, request);
	}

	/**
	 * Удаляет товар из корзины пользователя.
	 *
	 * @param header  Заголовок Authorization, который содержит в себе JSON Web Token.
	 * @param request Запрос с данными на удаление товара по id и его количества из корзины.
	 * @return Объект InfoResponse с информацией и статусом об удалении товара из корзины.
	 */
	@DeleteMapping
	public ResponseEntity<InfoResponse> removeItemFromCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String header,
														   @RequestBody CartRequest request) {
		String token = header.replace(JwtHeader.BEARER, "");
		int userId = jwtService.extractUserId(token);

		return cartService.removeItemFromUsersShoppingCart(userId, request);
	}
}
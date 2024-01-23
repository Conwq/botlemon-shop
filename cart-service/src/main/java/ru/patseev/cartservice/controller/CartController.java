package ru.patseev.cartservice.controller;

import jakarta.validation.Valid;
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
	 * @param authHeader Заголовок Authorization, который содержит в себе JSON Web Token.
	 * @return Список товаров, добавленных пользователем.
	 */
	@GetMapping
	public List<ItemDto> getUserShoppingCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		int userId = extractUserId(authHeader);

		return cartService.getUsersShoppingCart(userId);
	}

	/**
	 * Добавляет товар в корзину пользователя.
	 *
	 * @param authHeader Заголовок Authorization, который содержит в себе JSON Web Token.
	 * @param request    Запрос с данными на добавление товара и его количества в корзину.
	 * @return Объект InfoResponse с информацией и статусом о добавлении товара в корзину.
	 */
	@PostMapping
	public ResponseEntity<InfoResponse> addItemToCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
													  @Valid
													  @RequestBody CartRequest request) {
		int userId = extractUserId(authHeader);

		return cartService.addItemToUsersShoppingCart(userId, request);
	}

	/**
	 * Удаляет товар из корзины пользователя.
	 *
	 * @param authHeader Заголовок Authorization, который содержит в себе JSON Web Token.
	 * @param request    Запрос с данными на удаление товара по id и его количества из корзины.
	 * @return Объект InfoResponse с информацией и статусом об удалении товара из корзины.
	 */
	@DeleteMapping
	public ResponseEntity<InfoResponse> removeItemFromCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
														   @RequestBody CartRequest request) {
		int userId = extractUserId(authHeader);

		return cartService.removeItemFromUsersShoppingCart(userId, request);
	}

	/**
	 * Удаляет все корзины пользователя.
	 *
	 * @param authHeader Заголовок Authorization, который содержит в себе JSON Web Token.
	 */
	@DeleteMapping("/delete_cart")
	public void removeUsersCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		int userId = extractUserId(authHeader);
		cartService.removeUsersCart(userId);
	}

	private int extractUserId(String authHeader) {
		final String token = authHeader.replace(JwtHeader.BEARER, "");
		return jwtService.extractUserId(token);
	}
}
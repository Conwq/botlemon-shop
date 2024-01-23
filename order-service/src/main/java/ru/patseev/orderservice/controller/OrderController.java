package ru.patseev.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.patseev.orderservice.service.OrderService;

/**
 * Контроллер для работы с сервисом заказов.
 */
@RestController
@RequestMapping("/v1/api/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	/**
	 * Размещает заказ пользователя.
	 *
	 * @param authHeader Заголовок авторизации, который содержит Jwt токен.
	 * @return Возвращает ответ с сообщением и статусом операции.
	 */
	@PostMapping("/place")
	public ResponseEntity<String> placeOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		orderService.placeOrder(authHeader);
		return ResponseEntity.ok("Заказ успешно оформлен");
	}
}
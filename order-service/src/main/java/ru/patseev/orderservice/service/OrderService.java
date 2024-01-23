package ru.patseev.orderservice.service;

/**
 * Сервис по работе с заказами.
 */
public interface OrderService {

	/**
	 * Оформляет и размещает заказ.
	 *
	 * @param authHeader Заголовок авторизации, который содержит Jwt токен.
	 */
	void placeOrder(String authHeader);
}
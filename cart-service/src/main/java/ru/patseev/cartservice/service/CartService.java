package ru.patseev.cartservice.service;

import org.springframework.http.ResponseEntity;
import ru.patseev.cartservice.dto.CartRequest;
import ru.patseev.cartservice.dto.InfoResponse;
import ru.patseev.cartservice.dto.ItemDto;

import java.util.List;

/**
 * Сервис для работы с корзиной пользователя.
 */
public interface CartService {

	/**
	 * Получает корзину пользователя по его идентификатору.
	 *
	 * @param userId Идентификатор пользователя.
	 * @return Список товаров в корзине пользователя.
	 */
	List<ItemDto> getUsersShoppingCart(int userId);

	/**
	 * Добавляет товар в корзину пользователя.
	 *
	 * @param userId  Идентификатор пользователя.
	 * @param request Запрос на добавление товара в корзину.
	 * @return Ответ с информацией об операции и статус кодом.
	 */
	ResponseEntity<InfoResponse> addItemToUsersShoppingCart(int userId, CartRequest request);

	/**
	 * Удаляет товар из корзины пользователя.
	 *
	 * @param userId  Идентификатор пользователя.
	 * @param request Запрос на удаление товара из корзины.
	 * @return Ответ с информацией об операции и статус кодом.
	 */
	ResponseEntity<InfoResponse> removeItemFromUsersShoppingCart(int userId, CartRequest request);

	/**
	 * Удаляет все существующие корзины пользователя.
	 *
	 * @param userId Идентификатор пользователя, чья корзина будет удалена.
	 */
	void removeUsersCart(int userId);
}
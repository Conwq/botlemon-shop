package ru.patseev.storageservice.service;

import org.springframework.http.ResponseEntity;

/**
 * Сервиса управления складом товаров.
 */
public interface StorageService {

	/**
	 * Получает количество товара на складе по его идентификатору.
	 *
	 * @param itemId идентификатор товара
	 * @return количество товара на складе или {@code null}, если товар не найден
	 */
	Integer getQuantityForItem(int itemId);

	/**
	 * Добавляет указанное количество товара на склад.
	 *
	 * @param itemId   идентификатор товара
	 * @param quantity количество товара для добавления
	 * @return {@link ResponseEntity} с результатом операции
	 */
	ResponseEntity<Object> addQuantityItemToStorage(int itemId, int quantity);

	/**
	 * Изменяет количество товара на складе.
	 *
	 * @param itemId   идентификатор товара
	 * @param quantity новое количество товара
	 * @return {@link ResponseEntity} с результатом операции
	 */
	ResponseEntity<Object> editItemQuantity(int itemId, int quantity);

	/**
	 * Удаляет товар из хранилища по его идентификатору.
	 *
	 * @param itemId идентификатор товара
	 * @return {@link ResponseEntity} с результатом операции
	 */
	ResponseEntity<Object> removeItemQuantityFromStorage(int itemId);

	/**
	 * Возвращает указанное количество товара из корзины на склад.
	 *
	 * @param itemId   идентификатор товара
	 * @param quantity количество товара для возврата
	 * @return {@link ResponseEntity} с результатом операции
	 */
	ResponseEntity<Object> returnQuantityOfItemFromCartToStorage(int itemId, int quantity);

	/**
	 * Добавляет указанное количество товара в корзину, уменьшая его количество на складе.
	 *
	 * @param itemId   идентификатор товара
	 * @param quantity количество товара для добавления в корзину
	 * @return {@link ResponseEntity} с результатом операции
	 */
	ResponseEntity<Object> addItemQuantityToCart(int itemId, int quantity);
}
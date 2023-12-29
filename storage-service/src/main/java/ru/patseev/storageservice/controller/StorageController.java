package ru.patseev.storageservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.storageservice.dto.StorageRequest;
import ru.patseev.storageservice.service.StorageService;

/**
 * Контроллер, который содержит в себе операции для работы в хранилище.
 */
@RestController
@RequestMapping("/v1/api/storage")
@RequiredArgsConstructor
public class StorageController {
	private final StorageService storageService;

	/**
	 * Endpoint, который возвращает со склада количество товара по его id.
	 *
	 * @param itemId Id товара, для которого нужно получить количество.
	 * @return Количество товара.
	 */
	@GetMapping("/{itemId}")
	public Integer getQuantityForItem(@PathVariable("itemId") int itemId) {
		return storageService.getQuantityForItem(itemId);
	}

	/**
	 * Добавляет количество товара в хранилище, при добавлении товара в магазин
	 *
	 * @param request Запрос на добавлении товара. В нем содержится id товара и количество, которое нужно добавить.
	 * @return Статус с кодом выполнения.
	 */
	@PostMapping
	public ResponseEntity<Object> addQuantityItemToStorage(@RequestBody StorageRequest request) {
		return storageService.addQuantityItemToStorage(request.itemId(), request.quantity());
	}

	/**
	 * Удаляет количество товара из хранилища, при удалении самого товара.
	 *
	 * @param itemId Id удаляемого товара.
	 * @return Статус с кодом выполнения.
	 */
	@DeleteMapping("/{itemId}")
	public ResponseEntity<Object> removeItemQuantityFromStorage(@PathVariable("itemId") int itemId) {
		return storageService.removeItemQuantityFromStorage(itemId);
	}

	/**
	 * Обновляет количество товара в хранилище новыми данными.
	 *
	 * @param request Запрос на обновление количества товара. В нем содержится id товара и новое значение количества товара.
	 * @return Статус с кодом выполнения.
	 */
	@PutMapping("/edit")
	public ResponseEntity<Object> editItemQuantity(@RequestBody StorageRequest request) {
		return storageService.editItemQuantity(request.itemId(), request.quantity());
	}

	//Этот конечная точка работает с Cart Service
	/**
	 * Возвращает количество товара к количеству, которое есть в хранилище.
	 *
	 * @param request Запрос на возврат количества товара обратно в хранилище.
	 *                В нем содержится id товара и значение, которое нужно вернуть обратно в хранилище.
	 * @return Статус с кодом выполнения.
	 */
	@PutMapping("/return")
	public ResponseEntity<Object> returnQuantityOfItemToStorage(@RequestBody StorageRequest request) {
		return storageService.returnQuantityOfItemFromCartToStorage(request.itemId(), request.quantity());
	}

	//Эта конечная точка работает с Cart Service
	/**
	 * Забирает указанное количество товара из хранилища.
	 *
	 * @param request Запрос на получение количества указанного товара обратно из хранилища.
	 * @return Статус с кодом выполнения.
	 */
	@PatchMapping
	public ResponseEntity<Object> addItemQuantityToCart(@RequestBody StorageRequest request) {
		return storageService.addItemQuantityToCart(request.itemId(), request.quantity());
	}
}
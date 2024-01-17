package ru.patseev.itemservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.itemservice.dto.InfoResponse;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.service.ItemService;

import java.util.List;

/**
 * Контроллер для работы с товарами.
 */
@RestController
@RequestMapping("/v1/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	/**
	 * Получает информацию о товаре по его идентификатору.
	 *
	 * @param itemId Идентификатор товара.
	 * @param header Заголовок с токеном авторизации (необязательный).
	 * @return Объект DTO с информацией о товаре.
	 */
	@GetMapping("/item/{itemId}")
	public ItemDto getItem(@PathVariable("itemId") int itemId,
						   @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String header) {
		return itemService.getItemById(itemId, header);
	}

	/**
	 * Получает список всех товаров.
	 *
	 * @param header Заголовок с токеном авторизации (необязательный).
	 * @return Список объектов DTO с информацией о товарах.
	 */
	@GetMapping("/all_items")
	public List<ItemDto> getItemsList(@RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String header) {
		return itemService.getAllItems(header);
	}

	/**
	 * Добавляет новый товар.
	 *
	 * @param itemDto Объект DTO с информацией о новом товаре.
	 * @return Ответ с информацией о результате операции.
	 */
	@PostMapping("/add")
	public ResponseEntity<InfoResponse> addItem(@Valid @RequestBody ItemDto itemDto) {
		return itemService.addItem(itemDto);
	}

	/**
	 * Редактирует существующий товар.
	 *
	 * @param itemDto Объект DTO с информацией для редактирования товара.
	 * @return Ответ с информацией о результате операции.
	 */
	@PutMapping("/edit")
	public ResponseEntity<InfoResponse> editItem(@Valid @RequestBody ItemDto itemDto) {
		return itemService.editItem(itemDto);
	}

	/**
	 * Удаляет товар по его идентификатору.
	 *
	 * @param itemId Идентификатор товара для удаления.
	 * @return Ответ с информацией о результате операции.
	 */
	@DeleteMapping("/delete/{itemId}")
	public ResponseEntity<InfoResponse> deleteItem(@PathVariable("itemId") int itemId) {
		return itemService.deleteItem(itemId);
	}
}
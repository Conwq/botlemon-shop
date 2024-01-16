package ru.patseev.itemservice.service;

import org.springframework.http.ResponseEntity;
import ru.patseev.itemservice.dto.InfoResponse;
import ru.patseev.itemservice.dto.ItemDto;

import java.util.List;

/**
 * Сервис по работе с предметами.
 */
public interface ItemService {

	/**
	 * Получает предмет по идентификатору.
	 *
	 * @param itemId Идентификатор предмета.
	 * @return Возвращает предмет.
	 */
	ItemDto getItemById(int itemId);

	/**
	 * Получает все доступные предметы.
	 *
	 * @return Список предметов.
	 */
	List<ItemDto> getAllItems();

	/**
	 * Сохраняет предмет в БД и его количество через сторонний клиент Storage.
	 *
	 * @param itemDto Предмет, который добавляем в БД.
	 */
	ResponseEntity<InfoResponse> addItem(ItemDto itemDto);

	/**
	 * Удаляет предмет.
	 *
	 * @param itemId Идентификатор удаляемого предмета.
	 * @return Возвращает информацию по удалению со статусом.
	 */
	ResponseEntity<InfoResponse> deleteItem(int itemId);

	/**
	 * Изменяет предмет.
	 *
	 * @param itemDto Объект с новыми данными.
	 * @return Статус об изменении.
	 */
	ResponseEntity<InfoResponse> editItem(ItemDto itemDto);
}
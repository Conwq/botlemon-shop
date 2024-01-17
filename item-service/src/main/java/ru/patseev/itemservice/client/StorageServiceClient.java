package ru.patseev.itemservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.dto.StorageRequest;
import ru.patseev.itemservice.exception.ItemNotFoundException;

import java.util.Objects;

/**
 * Клиент для отправки запросов на получение\отправления данных на User service.
 */
@Component
@RequiredArgsConstructor
public class StorageServiceClient {
	private final WebClient.Builder webClientBuilder;

	/**
	 * Обновляет количество товара на складе.
	 *
	 * @param itemDto Объект, в котором находятся данные для отправки(id предмета и его количество)
	 */
	public void updateItemQuantityInStorage(ItemDto itemDto) {
		ResponseEntity<Object> response = webClientBuilder.build()
				.put()
				.uri("lb://storage-service/v1/api/storage/edit")
				.bodyValue(new StorageRequest(itemDto.getId(), itemDto.getQuantity()))
				.retrieve()
				.toEntity(Object.class)
				.block();

		if (Objects.requireNonNull(response).getStatusCode().is4xxClientError()) {
			throw new ItemNotFoundException();
		}
	}

	/**
	 * Получение количество предмета со склада.
	 *
	 * @param itemId Идентификатор предмета.
	 * @return Возвращает количество предмета.
	 */
	public int getQuantityItemFromStorage(int itemId) {
		return webClientBuilder.build()
				.get()
				.uri("lb://storage-service/v1/api/storage/{itemId}",
						uriBuilder -> uriBuilder.build(itemId))
				.retrieve()
				.bodyToMono(Integer.class)
				.blockOptional()
				.orElseThrow(ItemNotFoundException::new);
	}

	/**
	 * Сохраняет количество предмета на складе.
	 *
	 * @param itemId Идентификатор предмета.
	 * @param itemQuantity Количество предмета.
	 */
	public void saveQuantityItemToStorage(int itemId, int itemQuantity) {
		ResponseEntity<Object> response = webClientBuilder.build()
				.post()
				.uri("lb://storage-service/v1/api/storage")
				.bodyValue(new StorageRequest(itemId, itemQuantity))
				.retrieve()
				.toEntity(Object.class)
				.block();

		if (Objects.requireNonNull(response).getStatusCode().is4xxClientError()) {
			throw new ItemNotFoundException();
		}
	}

	/**
	 * Удаляет количество предмета со склада.
	 *
	 * @param itemId Идентификатор предмета.
	 */
	public void deleteQuantityItemFromStorage(int itemId) {
		ResponseEntity<Object> response = webClientBuilder.build()
				.delete()
				.uri("lb://storage-service/v1/api/storage/{itemId}",
						uriBuilder -> uriBuilder.build(itemId))
				.retrieve()
				.toEntity(Object.class)
				.block();

		if (Objects.requireNonNull(response).getStatusCode().is4xxClientError()) {
			throw new ItemNotFoundException();
		}
	}
}
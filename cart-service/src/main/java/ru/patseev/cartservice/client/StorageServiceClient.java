package ru.patseev.cartservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.cartservice.dto.StorageRequest;
import ru.patseev.cartservice.exception.UnacceptableQualityItemsException;

import java.util.Objects;

/**
 * Клиент для взаимодействия с сервисом хранения (Storage service).
 */
@Component
@RequiredArgsConstructor
public class StorageServiceClient {
	private final WebClient.Builder webClientBuilder;

	/**
	 * Получает доступное количество конкретного товара на складе.
	 *
	 * @param itemId    Идентификатор товара для проверки.
	 * @throws UnacceptableQualityItemsException, если доступное количество меньше желаемого.
	 */
	public int getAvailableItemQuantity(int itemId) {
		return Objects.requireNonNull(
				webClientBuilder.build()
						.get()
						.uri("lb://storage-service/v1/api/storage/{itemId}",
								uriBuilder -> uriBuilder.build(itemId))
						.retrieve()
						.bodyToMono(Integer.class)
						.block());
	}

	/**
	 * Возвращает количество конкретного товара на склад.
	 *
	 * @param itemId       Идентификатор товара для возврата.
	 * @param quantityItem Количество товара для возврата.
	 * @return ResponseEntity, содержащий результат операции.
	 */
	public ResponseEntity<Object> returnQuantityOfItemToStorage(int itemId, int quantityItem) {
		return webClientBuilder.build()
				.put()
				.uri("lb://storage-service/v1/api/storage/return")
				.bodyValue(new StorageRequest(itemId, quantityItem))
				.retrieve()
				.toEntity(Object.class)
				.block();
	}

	/**
	 * Добавляет количество конкретного товара в корзину покупок.
	 *
	 * @param itemId       Идентификатор товара для добавления в корзину.
	 * @param quantityItem Количество товара для добавления в корзину.
	 * @return ResponseEntity, содержащий результат операции.
	 */
	public ResponseEntity<Object> addItemQuantityToCart(int itemId, int quantityItem) {
		return webClientBuilder.build()
				.patch()
				.uri("lb://storage-service/v1/api/storage")
				.bodyValue(new StorageRequest(itemId, quantityItem))
				.retrieve()
				.toEntity(Object.class)
				.block();
	}
}
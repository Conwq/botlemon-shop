package ru.patseev.cartservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.cartservice.dto.StorageRequest;
import ru.patseev.cartservice.exception.UnacceptableQualityItemsException;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StorageServiceClient {
	private final WebClient.Builder webClientBuilder;

	public void checkAvailableItemQuantity(int itemId, int quantity) {
		Integer itemQuantity = Objects.requireNonNull(
				webClientBuilder.build()
						.get()
						.uri("http://storage-service/v1/api/storage/{itemId}",
								uriBuilder -> uriBuilder.build(itemId))
						.retrieve()
						.bodyToMono(Integer.class)
						.block());

		if (itemQuantity - quantity < 0) {
			throw new UnacceptableQualityItemsException("Unacceptable quality of items");
		}
	}

	public ResponseEntity<Object> returnQuantityOfItemToStorage(int itemId, int quantityItem) {
		return webClientBuilder.build()
				.put()
				.uri("http://storage-service/v1/api/storage/return")
				.bodyValue(new StorageRequest(itemId, quantityItem))
				.retrieve()
				.toEntity(Object.class)
				.block();
	}

	public ResponseEntity<Object> addItemQuantityToCart(int itemId, int quantityItem) {
		return webClientBuilder.build()
				.patch()
				.uri("http://storage-service/v1/api/storage")
				.bodyValue(new StorageRequest(itemId, quantityItem))
				.retrieve()
				.toEntity(Object.class)
				.block();
	}
}
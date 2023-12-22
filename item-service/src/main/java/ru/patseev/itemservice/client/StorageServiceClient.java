package ru.patseev.itemservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.dto.StorageRequest;
import ru.patseev.itemservice.exception.ItemNotFoundException;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StorageServiceClient {
	private final WebClient.Builder webClientBuilder;

	public void updateStorageQuantity(ItemDto itemDto) {
		ResponseEntity<Object> response = webClientBuilder.build()
				.put()
				.uri("http://storage-service/v1/api/storage/edit")
				.bodyValue(new StorageRequest(itemDto.getId(), itemDto.getQuantity()))
				.retrieve()
				.toEntity(Object.class)
				.block();

		if (Objects.requireNonNull(response).getStatusCode().is4xxClientError()) {
			throw new ItemNotFoundException();
		}
	}

	public int getQuantityItem(int itemId) {
		return webClientBuilder.build()
				.get()
				.uri("http://storage-service/v1/api/storage/{itemId}",
						uriBuilder -> uriBuilder.build(itemId))
				.retrieve()
				.bodyToMono(Integer.class)
				.blockOptional()
				.orElseThrow(ItemNotFoundException::new);
	}

	public void saveQuantityItemToStorage(int itemId, int itemQuantity) {
		ResponseEntity<Object> response = webClientBuilder.build()
				.post()
				.uri("http://storage-service/v1/api/storage")
				.bodyValue(new StorageRequest(itemId, itemQuantity))
				.retrieve()
				.toEntity(Object.class)
				.block();

		if (Objects.requireNonNull(response).getStatusCode().is4xxClientError()) {
			throw new ItemNotFoundException();
		}
	}

	public void deleteQuantityItemFromStorage(int itemId) {
		ResponseEntity<Object> response = webClientBuilder.build()
				.delete()
				.uri("http://storage-service/v1/api/storage/{itemId}",
						uriBuilder -> uriBuilder.build(itemId))
				.retrieve()
				.toEntity(Object.class)
				.block();

		if (Objects.requireNonNull(response).getStatusCode().is4xxClientError()) {
			throw new ItemNotFoundException();
		}
	}
}
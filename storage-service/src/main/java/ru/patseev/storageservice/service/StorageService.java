package ru.patseev.storageservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.storageservice.domain.StorageEntity;
import ru.patseev.storageservice.repository.ItemRepository;
import ru.patseev.storageservice.repository.StorageRepository;

@Service
@RequiredArgsConstructor
public class StorageService {
	private final StorageRepository storageRepository;
	private final ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public Integer getQuantityForItem(int itemId) {
		return storageRepository
				.findByItemId(itemId)
				.map(StorageEntity::getQuantity)
				.orElse(null);
	}

	@Transactional
	public ResponseEntity<Object> addQuantityItemToStorage(int itemId, int quantity) {
		return itemRepository
				.findById(itemId)
				.map(itemEntity -> {
					StorageEntity storageEntity = StorageEntity.builder()
							.item(itemEntity)
							.quantity(quantity).build();
					itemEntity.setStorage(storageEntity);
					storageRepository.save(storageEntity);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	public ResponseEntity<Object> editItemQuantity(int itemId, int quantity) {
		return storageRepository
				.findByItemId(itemId)
				.map(storageEntity -> {
					storageEntity.setQuantity(quantity);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	public ResponseEntity<Object> removeItemQuantityFromStorage(int itemId) {
		return storageRepository
				.findByItemId(itemId)
				.map(storageEntity -> {
					storageRepository.delete(storageEntity);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	public ResponseEntity<Object> returnQuantityOfItemFromCartToStorage(int itemId, int quantity) {
		Integer itemQuantity = this.getQuantityForItem(itemId);
		if (itemQuantity == null) {
			return ResponseEntity.notFound().build();
		}
		int newItemQuantity = quantity + itemQuantity;
		return this.editItemQuantity(itemId, newItemQuantity);
	}

	//TODO переписать логику по проверки доступности количества товара тут, для того чтобы не обращаться 2 раза в этот сервис
	@Transactional
	public ResponseEntity<Object> addItemQuantityToCart(int itemId, int quantity) {
		return storageRepository
				.findByItemId(itemId)
				.map(storageEntity -> {
					int newQuantity = storageEntity.getQuantity() - quantity;
					storageEntity.setQuantity(newQuantity);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}
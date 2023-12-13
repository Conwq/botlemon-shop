package ru.patseev.storageservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.storageservice.domain.ItemEntity;
import ru.patseev.storageservice.domain.StorageEntity;
import ru.patseev.storageservice.exception.ItemNotFoundException;
import ru.patseev.storageservice.repository.ItemRepository;
import ru.patseev.storageservice.repository.StorageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {

	private final StorageRepository storageRepository;

	private final ItemRepository itemRepository;

	@Transactional(readOnly = true)
	public int checkQuantityItemInStorage(int itemId) {
		return this.getStorageEntityByItemId(itemId)
				.getQuantity();
	}

	@Transactional
	public ResponseEntity<Object> addQuantityForItem(int itemId, int quantity) {
		Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);

		if (optionalItem.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		StorageEntity storageEntity = StorageEntity
				.builder()
				.item(optionalItem.get())
				.quantity(quantity)
				.build();

		storageRepository.save(storageEntity);
		return ResponseEntity.ok().build();
	}

	@Transactional
	public void addItemQuantityToCart(int itemId, int quantity) {
		StorageEntity storageEntity = this.getStorageEntityByItemId(itemId);
		int newQuantity = storageEntity.getQuantity() - quantity;

		storageEntity.setQuantity(newQuantity);
	}

	@Transactional
	public void removeQuantityForItem(int itemId) {
		int storageId = this.getStorageEntityByItemId(itemId).getId();

		storageRepository.deleteById(storageId);
	}

	@Transactional
	public void editItemQuantity(int itemId, int quantity) {
		this.getStorageEntityByItemId(itemId)
				.setQuantity(quantity);
	}

	@Transactional
	public void returnQuantityOfItemToStorage(int itemId, int quantity) {
		int itemQuantity = this.checkQuantityItemInStorage(itemId);
		int newItemQuantity = quantity + itemQuantity;
		this.editItemQuantity(itemId, newItemQuantity);
	}

	private StorageEntity getStorageEntityByItemId(int itemId) {
		return storageRepository
				.findByItemId(itemId)
				.orElseThrow(() -> new ItemNotFoundException("Item not found"));
	}
}
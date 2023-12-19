package ru.patseev.storageservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.storageservice.dto.StorageRequest;
import ru.patseev.storageservice.service.StorageService;

@RestController
@RequestMapping("/v1/api/storage")
@RequiredArgsConstructor
public class StorageController {
	private final StorageService storageService;

	@GetMapping("/{itemId}")
	public Integer getQuantityForItem(@PathVariable("itemId") int itemId) {
		return storageService.getQuantityForItem(itemId);
	}

	@PostMapping
	public ResponseEntity<Object> addQuantityForItem(@RequestBody StorageRequest request) {
		return storageService.addQuantityForItem(request.itemId(), request.quantity());
	}

	@DeleteMapping("/{itemId}")
	public ResponseEntity<Object> removeQuantityForItem(@PathVariable("itemId") int itemId) {
		return storageService.removeQuantityForItem(itemId);
	}

	@PutMapping("/edit")
	public ResponseEntity<Object> editItemQuantity(@RequestBody StorageRequest request) {
		return storageService.editItemQuantity(request.itemId(), request.quantity());
	}

	@PutMapping("/return")
	public ResponseEntity<Object> returnQuantityOfItemToStorage(@RequestBody StorageRequest request) {
		return storageService.returnQuantityOfItemToStorage(request.itemId(), request.quantity());
	}

	@PatchMapping
	public ResponseEntity<Object> addItemQuantityToCart(@RequestBody StorageRequest request) {
		return storageService.addItemQuantityToCart(request.itemId(), request.quantity());
	}
}
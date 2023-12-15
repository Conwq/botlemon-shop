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
	public int checkQuantityItemInStorage(@PathVariable("itemId") int itemId) {
		return storageService.checkQuantityItemInStorage(itemId);
	}

	@PostMapping
	public ResponseEntity<Object> addQuantityForItem(@RequestBody StorageRequest request) {
		return storageService.addQuantityForItem(request.itemId(), request.quantity());
	}

	@DeleteMapping("/{itemId}")
	public void removeQuantityForItem(@PathVariable("itemId") int itemId) {
		storageService.removeQuantityForItem(itemId);
	}

	@PutMapping("/edit")
	public void editItemQuantity(@RequestBody StorageRequest request) {
		storageService.editItemQuantity(request.itemId(), request.quantity());
	}

	@PutMapping("/return")
	public void returnQuantityOfItemToStorage(@RequestBody StorageRequest request) {
		storageService.returnQuantityOfItemToStorage(request.itemId(), request.quantity());
	}

	@PatchMapping
	public void addItemQuantityToCart(@RequestBody StorageRequest request) {
		storageService.addItemQuantityToCart(request.itemId(), request.quantity());
	}
}
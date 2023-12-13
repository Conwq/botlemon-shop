package ru.patseev.storageservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

	@PostMapping("/{itemId}/{quantity}")
	public ResponseEntity<Object> addQuantityForItem(@PathVariable("itemId") int itemId,
													 @PathVariable("quantity") int quantity) {
		return storageService.addQuantityForItem(itemId, quantity);
	}

	@DeleteMapping("/{itemId}/{quantity}")
	public void addItemQuantityToCart(@PathVariable("itemId") int itemId,
									  @PathVariable("quantity") int quantity) {
		storageService.addItemQuantityToCart(itemId, quantity);
	}

	@DeleteMapping("/{itemId}")
	public void removeQuantityForItem(@PathVariable("itemId") int itemId) {
		storageService.removeQuantityForItem(itemId);
	}

	@PutMapping("/{itemId}/{quantity}")
	public void editItemQuantity(@PathVariable("itemId") int itemId,
								 @PathVariable("quantity") int quantity) {
		storageService.editItemQuantity(itemId, quantity);
	}

	@PutMapping("/return/{itemId}/{quantity}")
	public void returnQuantityOfItemToStorage(@PathVariable("itemId") int itemId,
											  @PathVariable("quantity") int quantity) {
		storageService.returnQuantityOfItemToStorage(itemId, quantity);
	}
}
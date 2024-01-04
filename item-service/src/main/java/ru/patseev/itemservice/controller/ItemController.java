package ru.patseev.itemservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.itemservice.dto.InfoResponse;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/items")
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;

	@GetMapping("/item/{itemId}")
	public ItemDto getItem(@PathVariable("itemId") int itemId) {
		return itemService.getItemById(itemId);
	}

	@GetMapping("/all_items")
	public List<ItemDto> getItemsList() {
		return itemService.getAllItems();
	}

	@PostMapping("/add")
	public ResponseEntity<InfoResponse> addItem(@Valid
												@RequestBody ItemDto itemDto) {
		return itemService.addItem(itemDto);
	}

	@PutMapping("/edit")
	public ResponseEntity<InfoResponse> editItem(@Valid
												 @RequestBody ItemDto itemDto) {
		return itemService.editItem(itemDto);
	}

	@DeleteMapping("/delete/{itemId}")
	public ResponseEntity<InfoResponse> deleteItem(@PathVariable("itemId") int id) {
		return itemService.deleteItem(id);
	}
}
package ru.patseev.itemservice.controller;

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

	@GetMapping("/{itemId}")
	public ItemDto getItem(@PathVariable("itemId") int itemId) {
		return itemService.getItemById(itemId);
	}

	@GetMapping("")
	public List<ItemDto> getItemsList() {
		return itemService.getAllItems();
	}

	@PostMapping("/add")
	public ResponseEntity<InfoResponse> addItem(@RequestBody ItemDto itemDto) {
		return itemService.addItem(itemDto);
	}

	@PutMapping("/edit")
	public ResponseEntity<InfoResponse> editItem(@RequestBody ItemDto itemDto) {
		return itemService.editItem(itemDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<InfoResponse> deleteItem(@PathVariable("id") int id) {
		return itemService.deleteItem(id);
	}
}
package ru.patseev.itemservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.patseev.itemservice.dto.ItemDto;
import ru.patseev.itemservice.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("/{id}/get")
	public ItemDto getItem(@PathVariable("id") int id) {
		return itemService.getItemById(id);
	}

	@GetMapping("/get")
	public List<ItemDto> getItemsList() {
		return itemService.getAllItems();
	}

	@PostMapping("/add")
	public ItemDto addItem(@RequestBody ItemDto itemDto) {
		return itemService.addItem(itemDto);
	}

	@PutMapping("/edit")
	public ItemDto editItem(@RequestBody ItemDto itemDto) {
		return itemService.editItem(itemDto);
	}

	@DeleteMapping("/{id}/delete")
	public ItemDto deleteItem(@PathVariable("id") int id) {
		itemService.deleteItem(id);
		return null;
	}
}
package ru.patseev.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.cartservice.dto.InfoResponse;
import ru.patseev.cartservice.dto.ItemDto;
import ru.patseev.cartservice.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@GetMapping("/{userId}")
	public List<ItemDto> getUserShoppingCart(@PathVariable("userId") int userId) {
		return cartService.getUserShoppingCart(userId);
	}

	@PostMapping("/{userId}/{itemId}")
	public ResponseEntity<InfoResponse> addItemToCart(@PathVariable("userId") int userId,
													  @PathVariable("itemId") int itemId) {
		return cartService.addItemToCart(userId, itemId);
	}

	@DeleteMapping("/{userId}/{itemId}")
	public ResponseEntity<InfoResponse> removeItemFromCart(@PathVariable("userId") int userId,
														   @PathVariable("itemId") int itemId) {
		return cartService.removeItemFromCart(userId, itemId);
	}
}
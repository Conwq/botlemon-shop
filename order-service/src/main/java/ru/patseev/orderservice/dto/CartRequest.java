package ru.patseev.orderservice.dto;

public record CartRequest(int itemId,
						  int quantity) {
}

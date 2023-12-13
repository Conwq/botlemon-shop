package ru.patseev.cartservice.dto;

import lombok.Builder;

@Builder
public record CartRequest(int itemId, int quantity) {
}

package ru.patseev.usersservice.dto;

import lombok.Builder;

@Builder
public record AuthorizationResponse (
		String message,
		String token,
		String username
){
}

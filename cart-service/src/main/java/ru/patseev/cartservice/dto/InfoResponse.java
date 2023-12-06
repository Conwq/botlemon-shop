package ru.patseev.cartservice.dto;

import org.springframework.http.HttpStatus;

public record InfoResponse(Actions action, HttpStatus status) {
}

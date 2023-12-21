package ru.patseev.reviewservice.dto;

import org.springframework.http.HttpStatus;

public record InfoResponse(Actions action, HttpStatus status) {
}

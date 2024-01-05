package ru.patseev.authenticationservice.dto;

import org.springframework.http.HttpStatus;

public record ExceptionResponse (String message, HttpStatus status, String createdAt){
}

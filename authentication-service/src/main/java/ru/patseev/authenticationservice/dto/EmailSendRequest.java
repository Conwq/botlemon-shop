package ru.patseev.authenticationservice.dto;

/**
 * DTO (Data Transfer Object) для запроса на отправку электронного письма.
 */
public record EmailSendRequest(String userEmail, String activationCode) {
}
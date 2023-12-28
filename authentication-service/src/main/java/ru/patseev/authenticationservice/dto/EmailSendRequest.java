package ru.patseev.authenticationservice.dto;

public record EmailSendRequest(String userEmail, String activationCode) {
}

package ru.patseev.emailsenderservice.dto;

public record EmailSendRequest(String userEmail, String activationCode) {
}


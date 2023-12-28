package ru.patseev.emailsenderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.patseev.emailsenderservice.dto.EmailSendRequest;
import ru.patseev.emailsenderservice.service.EmailSenderService;

@RestController
@RequestMapping("/v1/api/email")
@RequiredArgsConstructor
public class EmailSenderController {
	private final EmailSenderService emailSenderService;

	@PostMapping("/activate_account")
	public void sendEmailForActivateAccount(@RequestBody EmailSendRequest request) {
		emailSenderService.sendEmailForActivateAccountToUserEmail(request);
	}
}
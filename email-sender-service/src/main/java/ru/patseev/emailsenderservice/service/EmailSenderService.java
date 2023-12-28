package ru.patseev.emailsenderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.patseev.emailsenderservice.dto.EmailSendRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailSenderService {
	@Value("${spring.mail.username}")
	private String sender;
	private final JavaMailSender mailSender;

	public void sendEmailForActivateAccountToUserEmail(EmailSendRequest request) {
		SimpleMailMessage mail = this.createMessage(request);
		mailSender.send(mail);
		log.info("Email for activation -> {}", mail.getText());
	}

	private SimpleMailMessage createMessage(EmailSendRequest request) {
		String message = String.format("http://localhost:5354/v1/api/auth/activate/%s", request.activationCode());

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setSubject("Activation code");
		mail.setFrom(sender);
		mail.setTo(request.userEmail());
		mail.setText(message);

		return mail;
	}
}
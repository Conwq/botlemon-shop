package ru.patseev.authenticationservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.authenticationservice.dto.EmailSendRequest;

/**
 * Клиент для отправки электронных писем.
 */
@Component
@RequiredArgsConstructor
public class EmailSenderClient {
	private final WebClient.Builder webClientBuilder;

	/**
	 * Отправляет электронное письмо для подтверждения аккаунта пользователю.
	 *
	 * @param userEmail      Адрес электронной почты пользователя.
	 * @param activationCode Код активации аккаунта.
	 */
	public void sendEmailConfirmingAccountToUser(String userEmail, String activationCode) {
		EmailSendRequest emailSendRequest = new EmailSendRequest(userEmail, activationCode);

		//TODO добавить RabbitMQ вместо вот этой части
		webClientBuilder.build()
				.post()
				.uri("lb://email-sender-service/v1/api/email/activate_account")
				.bodyValue(emailSendRequest)
				.retrieve()
				.toBodilessEntity()
				.block();
	}
}
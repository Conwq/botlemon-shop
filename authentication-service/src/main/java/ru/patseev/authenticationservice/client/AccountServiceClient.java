package ru.patseev.authenticationservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Клиент для отправки запросов на AccountService.
 */
@Component
@RequiredArgsConstructor
public class AccountServiceClient {
	private final WebClient.Builder webClientBuilder;

	/**
	 * Отправляет запрос на обновление последнего входа в аккаунт.
	 *
	 * @param userId Идентификатор пользователя.
	 */
	public void sendRequestToUpdateLastLoginDate(int userId) {
		webClientBuilder.build()
				.method(HttpMethod.PATCH)
				.uri("lb://account-service/v1/api/account/update_login_date/{userId}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve()
				.toBodilessEntity()
				.subscribe();
	}
}
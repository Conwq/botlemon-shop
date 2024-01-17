package ru.patseev.itemservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.itemservice.dto.AccountDto;

/**
 * Клиент для отправки запросов на сервис account.
 */
@Component
@RequiredArgsConstructor
public class AccountServiceClient {
	private final WebClient.Builder webClientBuilder;

	/**
	 * Отправляет запрос на получение деталей аккаунта.
	 *
	 * @param header Заголовок авторизации - JWT токен.
	 * @return Возвращает детали аккаунта.
	 */
	public AccountDto sendRequestToReceiveAccountDetails(String header) {
		ResponseEntity<AccountDto> response = webClientBuilder.build()
				.method(HttpMethod.GET)
				.uri("lb://account-service/v1/api/account/details")
				.header(HttpHeaders.AUTHORIZATION, header)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve()
				.toEntity(AccountDto.class)
				.block();

		assert response != null;
		return response.getBody();
	}
}
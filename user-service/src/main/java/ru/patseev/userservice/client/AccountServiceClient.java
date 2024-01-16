package ru.patseev.userservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Клиент по отправке запросов на сервис Account service.
 */
@Component
@RequiredArgsConstructor
public class AccountServiceClient {
	private final RestTemplate restTemplate;

	/**
	 * Отправляет запрос на создание деталей аккаунта пользователя.
	 *
	 * @param userId Идентификатор пользователя, для кого буудет создаваться аккаунт.
	 */
	public void sendRequestToCreatePersonalAccountDetails(int userId) {
		restTemplate.postForObject(
				"http://localhost:5021/v1/api/account/create/{userId}",
				null,
				Void.class,
				userId
		);
	}
}
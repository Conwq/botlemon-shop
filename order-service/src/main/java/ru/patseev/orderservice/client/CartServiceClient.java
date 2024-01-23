package ru.patseev.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.patseev.orderservice.dto.ItemDto;

import java.util.List;

/**
 * Клиент для отправки запросов на Cart Service.
 */
@Component
@RequiredArgsConstructor
public class CartServiceClient {
	private final RestTemplate restTemplate;

	/**
	 * Отправляет запрос на получение корзины пользователя.
	 *
	 * @param authHeader Заголовок авторизации пользователя, который содержит Jwt токен.
	 * @return Список товаров пользователя из корзины.
	 */
	public List<ItemDto> sendRequestToReceiveUsersCart(String authHeader) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, authHeader);

		ResponseEntity<List<ItemDto>> response = restTemplate.exchange(
				"http://localhost:5012/v1/api/cart",
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {}
		);

		return response.getBody();
	}

	/**
	 * Отправляет запрос на удаление всей корзины пользователя.
	 *
	 * @param authHeader Заголовок авторизации пользователя, который содержит Jwt токен.
	 */
	public void sendRequestToRemoveItemFromCart(String authHeader) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, authHeader);

		restTemplate.exchange(
				"http://localhost:5012/v1/api/cart/delete_cart",
				HttpMethod.DELETE,
				new HttpEntity<>(headers),
				Void.class
		);
	}
}
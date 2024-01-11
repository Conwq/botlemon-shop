package ru.patseev.authenticationservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.authenticationservice.dto.UserDto;

import java.util.Optional;

/**
 * Клиент для взаимодействия с сервисом пользователей.
 */
@Component
@RequiredArgsConstructor
public class UserServiceClient {
	private final WebClient.Builder webClientBuilder;

	/**
	 * Отправляет данные пользователя для сохранения.
	 *
	 * @param userDto Данные пользователя.
	 */
	public void sendUserDataForSaving(UserDto userDto) {
		webClientBuilder.build()
				.method(HttpMethod.POST)
				.uri("lb://user-service/v1/api/users/save")
				.bodyValue(userDto)
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	/**
	 * Отправляет запрос для получения учетных данных пользователя по его имени пользователя.
	 *
	 * @param username Имя пользователя.
	 * @return Optional с учетными данными пользователя.
	 */
	public Optional<UserDto> sendRequestToReceiveUserCredentials(String username) {
		ResponseEntity<Optional<UserDto>> response = webClientBuilder.build()
				.method(HttpMethod.GET)
				.uri("lb://user-service/v1/api/users/user/{username}", username)
				.retrieve()
				.toEntity(new ParameterizedTypeReference<Optional<UserDto>>() {})
				.block();

		assert response != null;
		return response.getBody();
	}

	/**
	 * Отправляет запрос для проверки существования пользователя по его имени пользователя.
	 *
	 * @param username Имя пользователя.
	 * @return true, если пользователь существует, в противном случае - false.
	 */
	public boolean sendRequestToCheckExistenceUsername(String username) {
		String uriWithParameter = String.format("lb://user-service/v1/api/users/check_username?username=%s", username);

		ResponseEntity<Boolean> response = webClientBuilder.build()
				.method(HttpMethod.GET)
				.uri(uriWithParameter)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve()
				.toEntity(Boolean.class)
				.block();

		assert response != null;
		return Boolean.TRUE.equals(response.getBody());
	}

	/**
	 * Отправляет запрос для проверки существования пользователя по его адресу электронной почты.
	 *
	 * @param email Адрес электронной почты пользователя.
	 * @return true, если пользователь существует, в противном случае - false.
	 */
	public boolean sendRequestToCheckExistenceEmail(String email) {
		String uriWithParameter = String.format("lb://user-service/v1/api/users/check_email?email=%s", email);

		ResponseEntity<Boolean> response = webClientBuilder.build()
				.method(HttpMethod.GET)
				.uri(uriWithParameter)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve()
				.toEntity(Boolean.class)
				.block();

		assert response != null;
		return Boolean.TRUE.equals(response.getBody());
	}

	/**
	 * Отправляет запрос для активации учетной записи пользователя по коду активации.
	 *
	 * @param activationCode Код активации.
	 * @return true, если активация успешна, в противном случае - false.
	 */
	public boolean sendRequestToActivateAccount(String activationCode) {
		ResponseEntity<Boolean> response = webClientBuilder.build()
				.method(HttpMethod.GET)
				.uri("lb://user-service/v1/api/users/activate_account/{activationCode}", activationCode)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve()
				.toEntity(Boolean.class)
				.block();

		assert response != null;
		return Boolean.TRUE.equals(response.getBody());
	}
}
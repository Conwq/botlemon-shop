package ru.patseev.accountservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.patseev.accountservice.dto.UserDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
	private final WebClient.Builder webClientBuilder;

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
}
package ru.patseev.apigateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.patseev.apigateway.configuration.RouteValidator;
import ru.patseev.jwtservice.starter.service.JwtHeader;
import ru.patseev.jwtservice.starter.service.JwtService;

import java.util.function.Predicate;

/**
 * Фильтр, предназначенный для проверки доступа пользователей и авторизации к сервисам.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GatewayFilter {
	private final JwtService jwtService;
	private final RouteValidator routeValidator;

	/**
	 * Фильтр аутентификации и авторизации для маршрутов API.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (routeValidator.isSecured.test(request)) {
			if (this.authHeaderMissing(request)) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			final String token = this.extractTokenFromRequest(request);

			if (!this.accessAllowed(request, token)) {
				return onError(exchange, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return chain.filter(exchange);
	}

	/*
	 * Извлекает роль пользователя из токена,
	 * после чего проверяет, разрешен ли доступ пользователя к запрошенному маршруту на основе роли в токене
	 */
	private boolean accessAllowed(ServerHttpRequest request, String token) {
		String userRole = jwtService.extractUserRole(token);
		String path = request.getURI().getPath();

		Predicate<String> isAccessAllowed = routeValidator.createAccessCheckPredicate.apply(userRole);

		return isAccessAllowed.test(path);
	}

	/*
	 * Обрабатывает ошибку и возвращает ответ с указанным статусом.
	 */
	private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	/*
	 * Извлекает токен из запроса пользователя.
	 */
	private String extractTokenFromRequest(ServerHttpRequest request) {
		String bearer = request
				.getHeaders()
				.getOrEmpty(HttpHeaders.AUTHORIZATION)
				.get(0);

		return bearer.substring(7);
	}

	/*
	 * Проверяет на отсутствие заголовка авторизации в запросе пользователя.
	 */
	private boolean authHeaderMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION) ||
				!request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).startsWith(JwtHeader.BEARER);
	}
}
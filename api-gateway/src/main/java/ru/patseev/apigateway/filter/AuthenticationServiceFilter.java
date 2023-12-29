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
import ru.patseev.jwtservice.starter.service.JwtHeader;

/**
 * Фильтр для сервиса аутентификации.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationServiceFilter implements GatewayFilter {

	/**
	 * Фильтрует входящий запрос, проверяя наличие заголовка авторизации.
	 * Создать его для того, чтобы только не аутентифицированные пользователи могли воспользоваться данным сервисом.
	 * Для авторизированных пользователей он будет недоступен.
	 *
	 * @param exchange   Обмен данными между клиентом и сервером.
	 * @param chain      Цепочка фильтров.
	 * @return           Результат выполнения фильтрации.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (this.isAuthHeaderPresent(request)) {
			return onError(exchange);
		}
		return chain.filter(exchange);
	}

	/**
	 * Обработка ошибки аутентификации.
	 *
	 * @param exchange      Обмен данными между клиентом и сервером.
	 * @return              Результат выполнения обработки ошибки.
	 */
	private Mono<Void> onError(ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.FORBIDDEN);
		return response.setComplete();
	}

	/**
	 * Проверка наличия и корректности заголовка авторизации.
	 *
	 * @param request  Входящий HTTP-запрос.
	 * @return         false, если заголовок авторизации некорректен или отсутствует, иначе true.
	 */
	private boolean isAuthHeaderPresent(ServerHttpRequest request) {
		return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION) &&
				request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).startsWith(JwtHeader.BEARER);
	}
}
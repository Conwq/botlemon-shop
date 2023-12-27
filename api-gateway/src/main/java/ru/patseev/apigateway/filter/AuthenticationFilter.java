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
import ru.patseev.apigateway.service.JwtService;

import java.util.Collections;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
	private final RouteValidator routeValidator;
	private final JwtService jwtService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (routeValidator.isSecured.test(request)) {
			if (this.authHeaderMissing(request)) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			final String token = this.getTokenFromRequest(request);

			if (jwtService.isTokenExpired(token)) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			if (!this.accessAllowed(request, token)) {
				return onError(exchange, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return chain.filter(exchange);
	}

	private boolean accessAllowed(ServerHttpRequest request, String token) {
		String userRole = jwtService.extractUserRole(token);
		String path = request.getURI().getPath();

		Set<String> accessRoles = routeValidator.closeApiEndpoints.getOrDefault(path, Collections.emptySet());
		return accessRoles.contains(userRole);
	}

	private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private boolean authHeaderMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION) ||
				!request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).startsWith("Bearer ");
	}

	private String getTokenFromRequest(ServerHttpRequest request) {
		String bearer = request
				.getHeaders()
				.getOrEmpty(HttpHeaders.AUTHORIZATION)
				.get(0);

		return bearer.substring(7);
	}
}
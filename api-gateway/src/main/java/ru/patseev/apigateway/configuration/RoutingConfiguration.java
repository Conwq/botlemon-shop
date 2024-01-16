package ru.patseev.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.patseev.apigateway.filter.AuthenticationServiceFilter;
import ru.patseev.apigateway.filter.JwtAuthenticationFilter;

/**
 * Конфигурационный класс предназначенный для настройки маршрутизации конечных точек.
 */
@Configuration
@RequiredArgsConstructor
public class RoutingConfiguration {
	private final AuthenticationServiceFilter authenticationServiceFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * Создает и настраивает маршруты для различных сервисов с использованием фильтров и без них.
	 *
	 * @param builder Построитель маршрутов.
	 * @return Объект RouteLocator - обработчик маршрутов.
	 */
	@Bean
	public RouteLocator rotes(RouteLocatorBuilder builder) {
		return builder
				.routes()
				//port 5011
				.route("item-service",
						p -> p.path("/v1/api/items/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://item-service")
				)
				//port 5012
				.route("cart-service",
						p -> p.path("/v1/api/cart/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://cart-service")
				)
				//port 5014
				.route("storage-service",
						p -> p.path("/v1/api/storage/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://storage-service")
				)
				//port 5015
				.route("items-reviews-service",
						p -> p.path("/v1/api/review/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://items-reviews-service")
				)
				//port 5017
				.route("authentication-service",
						p -> p.path("/v1/api/auth/**")
								.filters(f -> f.filter(authenticationServiceFilter))
								.uri("lb://authentication-service")
				)
				//port 5018
				.route("email-sender-service",
						p -> p.path("/v1/api/email/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://email-sender-service")
				)
				//port 5020
				.route("user-service",
						p -> p.path("/v1/api/users/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://user-service")
				)
				//port 5021
				.route("account-service",
						p -> p.path("/v1/api/account/**")
								.filters(f -> f.filter(jwtAuthenticationFilter))
								.uri("lb://account-service"))
				//port 8761
				.route("discovery-server",
						p -> p.path("/eureka/web")
								.filters(f -> f.setPath("/"))
								.uri("http://localhost:8761")
				)
				.route("discovery-server-static",
						p -> p.path("/eureka/**")
								.uri("http://localhost:8761")
				)
				.build();
	}
}
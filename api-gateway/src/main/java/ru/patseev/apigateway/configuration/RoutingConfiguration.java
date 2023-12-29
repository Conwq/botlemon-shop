package ru.patseev.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.patseev.apigateway.filter.AuthenticationFilter;
import ru.patseev.apigateway.filter.ItemServiceFilter;

/**
 * Конфигурационный класс предназначенный для настройки маршрутизации конечных точек.
 */
@Configuration
@RequiredArgsConstructor
public class RoutingConfiguration {
	private final AuthenticationFilter authenticationFilter;
	private final ItemServiceFilter itemServiceFilter;

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
				.route("item-service",
						p -> p.path("/v1/api/items/**")
								.filters(f -> f.filter(itemServiceFilter))
								.uri("lb://item-service")
				)
//				.route("cart-service",
//						p -> p.path("/v1/api/cart/**")
//								.uri("lb://cart-service")
//				)
//				.route("user-service",
//						p -> p.path("/v1/api/users")
//								.uri("lb://user-service")
//				)
				.route("storage-service",
						p -> p.path("/v1/api/storage/**")
								.filters(f -> f.filter(itemServiceFilter))
								.uri("lb://storage-service")
				)
//				.route("items-reviews-service",
//						p -> p.path("/v1/api/review/**")
//								.uri("lb://items-reviews-service")
//				)
				.route("authentication-service",
						p -> p.path("/v1/api/auth/**")
								.filters(f -> f.filter(authenticationFilter))
								.uri("lb://authentication-service")
				)
				.route("email-sender-service",
						p -> p.path("/v1/api/email/**")
								.uri("lb://email-sender-service")
				)
//				.route("discovery-server",
//						p -> p.path("/eureka/web")
//								.filters(f -> f.setPath("/"))
//								.uri("http://localhost:8761")
//				)
//				.route("discovery-server-static",
//						p -> p.path("/eureka/**")
//								.uri("http://localhost:8761")
//				)
				.build();
	}
}
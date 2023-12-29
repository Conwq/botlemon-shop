package ru.patseev.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Класс предназначенный для обработки маршрутов.
 */
@Component
@RequiredArgsConstructor
public class RouteValidator {
	private static final Set<String> ADMIN_ROLE = Set.of("ADMIN");
	private static final Set<String> ALL_ROLES = Set.of("USER", "ADMIN");

	/**
	 * Набор открытых конечных точек, для которых не требуется никакие роли (авторизации).
	 */
	private final Set<String> openApiEndpoints = Set.of(

			//Authentication service
			"/v1/api/authorization",
			"/v1/api/auth/register",
			"/v1/api/auth/activate",

			//Item service
			"/v1/api/items/all_items",
			"/v1/api/items/item",

			//Storage service
			"/v1/api/storage"
	);

	/**
	 * Набор закрытых конечных точек, для которых требуется определенные роли пользователей для доступа.
	 */
	private final Map<String, Set<String>> closeApiEndpoints = Map.of(
			"/v1/api/items/add", ADMIN_ROLE,
			"/v1/api/items/edit", ADMIN_ROLE,
			"/v1/api/items/delete/", ADMIN_ROLE
	);

	/**
	 * Предикат для проверки конечной точки, является ли она открытой или закрытой.
	 */
	public final Predicate<ServerHttpRequest> isSecured =
			request -> openApiEndpoints.stream()
					.noneMatch(uri -> request
							.getURI()
							.getPath()
							.contains(uri));

	/**
	 * Функция для создания предиката проверки доступа к конкретным конечным точкам на основе роли пользователя.
	 */
	public final Function<String, Predicate<String>> createAccessCheckPredicate =
			userRole ->
					path -> closeApiEndpoints
							.entrySet()
							.stream()
							.filter(entry -> path.contains(entry.getKey()))
							.anyMatch(entry -> entry.getValue().contains(userRole));
}
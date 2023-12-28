package ru.patseev.apigateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class RouteValidator {
	private static final Set<String> ADMIN_ROLE = Set.of("ADMIN");
	private static final Set<String> ALL_ROLES = Set.of("USER", "ADMIN");

	public final List<String> openApiEndpoints = List.of(
			"/v1/api/items/all_items",
			"/v1/api/items/item"
	);

	public final Map<String, Set<String>> closeApiEndpoints = Map.of(

			"/v1/api/items/add", ADMIN_ROLE,
			"/v1/api/items/edit", ADMIN_ROLE,
			"/v1/api/items/delete", ADMIN_ROLE

			//
	);

	public Predicate<ServerHttpRequest> isSecured =
			request -> openApiEndpoints.stream()
					.noneMatch(uri -> request
							.getURI()
							.getPath()
							.contains(uri)
					);
}

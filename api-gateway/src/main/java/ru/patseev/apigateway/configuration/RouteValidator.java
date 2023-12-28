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
	private final Set<String> adminRole = Set.of("ADMIN");
	private final Set<String> allRoles = Set.of("USER", "ADMIN");

	public final List<String> openApiEndpoints = List.of(
			"/v1/api/auth/register",
			"/v1/api/auth/authorization",
			"/v1/api/auth/activate"
	);

	public final Map<String, Set<String>> closeApiEndpoints = Map.of(
			"/v1/api/auth/admin", adminRole,
			"/v1/api/auth/all", allRoles
	);

	public Predicate<ServerHttpRequest> isSecured =
			request -> openApiEndpoints.stream()
					.noneMatch(uri -> request
							.getURI()
							.getPath()
							.contains(uri)
					);
}

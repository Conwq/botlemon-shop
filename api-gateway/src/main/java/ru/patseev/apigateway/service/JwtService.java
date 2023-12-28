package ru.patseev.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Сервис для работы с JWT.
 */
@Service
public class JwtService {
	@Value("${spring.jwt.secret-key}")
	private String secret;

	/**
	 * Получает секретный ключ для подписи JWT из конфигурации.
	 *
	 * @return Секретный ключ для подписи JWT.
	 */
	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Извлекает все утверждения (claims) из JWT.
	 *
	 * @param token JWT токен.
	 * @return Все утверждения из токена.
	 */
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/**
	 * Проверяет, истек ли срок действия JWT токена.
	 *
	 * @param token JWT токен.
	 * @return {@code true}, если срок действия токена истек, в противном случае - {@code false}.
	 */
	public boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	/**
	 * Извлекает роль пользователя из JWT токена.
	 *
	 * @param token JWT токен.
	 * @return Роль пользователя.
	 */
	public String extractUserRole(String token) {
		return extractClaim(token, claim -> claim.get("role", String.class));
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
}
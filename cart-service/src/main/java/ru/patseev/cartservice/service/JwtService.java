package ru.patseev.cartservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.function.Function;

@Service
public class JwtService {
	@Value("${spring.jwt.secret-key}")
	private String secret;

	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public int extractUserId(String token) {
		return extractClaim(token, claim -> claim.get("userId", Integer.class));
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
}
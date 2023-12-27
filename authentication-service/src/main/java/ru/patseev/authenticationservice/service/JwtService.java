package ru.patseev.authenticationservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	@Value("${spring.jwt.secret-key}")
	private String secret;
	@Value("${spring.jwt.expiration}")
	private Long expiration;

	public String generateToken(Map<String, Object> claims, String username) {
		return createToken(claims, username);
	}

	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public int extractUserId(String token) {
		return extractClaim(token, claim -> claim.get("id", Integer.class));
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractUserRole(String token) {
		return extractClaim(token, claim -> claim.get("role", String.class));
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

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignKey(), Jwts.SIG.HS256)
				.compact();
	}
}
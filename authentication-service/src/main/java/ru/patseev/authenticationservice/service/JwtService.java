package ru.patseev.authenticationservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	@Value("${spring.jwt.secret-key}")
	private String SECRET_KEY;

	//TODO
	public boolean isTokenValid(String token) {
		return !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}

	public String generateToken(Map<String, Object> extraClaims, String username) {
		return createToken(extraClaims, username);
	}

	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSignKey(), Jwts.SIG.HS256)
				.compact();
	}

	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	//TODO
	private Claims extractAllClaims(String token) {
		return Jwts
				.parser()
				.setSigningKey(getSignKey())
				//.decryptWith(getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
}
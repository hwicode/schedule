package hwicode.schedule.auth.infra.token;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.infra.token.OauthUserNotValidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenProvider {

    private final String issuer;
    private final long accessTokenExpiryMs;
    private final long refreshTokenExpiryMs;
    private final Key secretKey;
    private final SecureRandom secureRandom;

    public TokenProvider (
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.access-token-expiry}") long accessTokenExpiry,
            @Value("${jwt.refresh-token-expiry}") long refreshTokenExpiry,
            @Value("${jwt.secret-key}") String secretKey
            ) {
        this.issuer = Objects.requireNonNull(issuer);
        this.accessTokenExpiryMs = accessTokenExpiry;
        this.refreshTokenExpiryMs = refreshTokenExpiry;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        secureRandom = new SecureRandom();
    }

    public String createAccessToken(OauthUser oauthUser) {
        validateOauthUserId(oauthUser.getId());
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpiryMs);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject("accessToken")
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("userId", oauthUser.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken createRefreshToken(OauthUser oauthUser) {
        validateOauthUserId(oauthUser.getId());
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getEncoder().encodeToString(randomBytes) + oauthUser.getId();
        return new RefreshToken(token, refreshTokenExpiryMs);
    }

    public DecodedAccessToken decodeAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = claims.get("userId", Long.class);
        validateOauthUserId(userId);
        return new DecodedAccessToken(userId);
    }

    private void validateOauthUserId(Long userId) {
        if (userId == null) {
            throw new OauthUserNotValidException();
        }
    }
}

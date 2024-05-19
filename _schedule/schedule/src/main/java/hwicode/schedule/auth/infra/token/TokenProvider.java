package hwicode.schedule.auth.infra.token;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.infra.token.InvalidTokenException;
import hwicode.schedule.auth.exception.infra.token.InvalidOauthUserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenProvider {

    private static final String USER_ID = "userId";
    private final String issuer;
    private final long accessTokenExpiryMs;
    private final long refreshTokenExpiryMs;
    private final Key secretKey;

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
                .claim(USER_ID, oauthUser.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken createRefreshToken(OauthUser oauthUser) {
        validateOauthUserId(oauthUser.getId());
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpiryMs);

        String token = Jwts.builder()
                .setIssuer(issuer)
                .setSubject("refreshToken")
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim(USER_ID, oauthUser.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new RefreshToken(token, refreshTokenExpiryMs);
    }

    public RefreshToken reissueRefreshToken(OauthUser oauthUser, String refreshToken) {
        validateOauthUserId(oauthUser.getId());
        Date now = new Date();
        Date validity = getClaims(refreshToken).getExpiration();

        String token = Jwts.builder()
                .setIssuer(issuer)
                .setSubject("refreshToken")
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim(USER_ID, oauthUser.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        long reissuedRefreshTokenExpiryMs = validity.getTime() - now.getTime();
        return new RefreshToken(token, reissuedRefreshTokenExpiryMs);
    }

    public DecodedToken decodeToken(String token) {
        Claims claims = getClaims(token);
        Long userId = claims.get(USER_ID, Long.class);
        validateOauthUserId(userId);
        return new DecodedToken(userId);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    private void validateOauthUserId(Long userId) {
        if (userId == null) {
            throw new InvalidOauthUserException();
        }
    }
}

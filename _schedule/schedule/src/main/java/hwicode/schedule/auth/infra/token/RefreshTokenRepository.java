package hwicode.schedule.auth.infra.token;

import hwicode.schedule.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN = "refresh-token:";

    public void save(Long userId, RefreshToken refreshToken) {
        long expiryMs = refreshToken.getExpiryMs();
        redisTemplate.opsForValue()
                .set(REFRESH_TOKEN + userId, refreshToken.getToken(), Duration.ofMillis(expiryMs));
    }

    public RefreshToken get(Long userId) {
        String token = redisTemplate.opsForValue()
                .get(REFRESH_TOKEN + userId);

        Long expiryMs = redisTemplate.getExpire(REFRESH_TOKEN + userId, TimeUnit.MICROSECONDS);
        return new RefreshToken(token, expiryMs);
    }
}

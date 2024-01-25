package hwicode.schedule.auth.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.application.dto.AuthTokenResponse;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.infra.client.OauthClientMapper;
import hwicode.schedule.auth.infra.client.UserInfo;
import hwicode.schedule.auth.infra.other_boundedcontext.UserConnector;
import hwicode.schedule.auth.infra.token.RefreshTokenRepository;
import hwicode.schedule.auth.infra.token.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    AuthService authService;

    @MockBean
    OauthClientMapper oauthClientMapper;

    @Autowired
    UserConnector userConnector;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();

        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    void OAuth를_통해_로그인할_수_있다() {
        // given
        UserInfo userInfo = new UserInfo("name", "email", OauthProvider.GOOGLE);
        OauthClient client = mock(OauthClient.class);
        when(client.getUserInfo(any())).thenReturn(userInfo);
        when(oauthClientMapper.getOauthClient(any())).thenReturn(client);

        // when
        AuthTokenResponse authTokenResponse = authService.loginWithOauth(OauthProvider.GOOGLE, "code");

        // then
        String accessToken = authTokenResponse.getAccessToken();
        Long userId = tokenProvider.decodeAccessToken(accessToken).getUserId();

        OauthUser oauthUser = userConnector.saveOrUpdate(userInfo.toEntity());
        assertThat(oauthUser.getId()).isEqualTo(userId);

        RefreshToken refreshToken = refreshTokenRepository.get(userId);
        assertThat(refreshToken.getToken()).isEqualTo(authTokenResponse.getRefreshToken());
    }

}

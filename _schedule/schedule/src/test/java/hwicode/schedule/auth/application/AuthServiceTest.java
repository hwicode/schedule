package hwicode.schedule.auth.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.application.dto.AuthTokenResponse;
import hwicode.schedule.auth.application.dto.ReissuedAuthTokenResponse;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.application.InvalidRefreshTokenException;
import hwicode.schedule.auth.exception.infra.other_boundedcontext.OauthUserNotFoundException;
import hwicode.schedule.auth.exception.infra.token.RefreshTokenNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        redisTemplate.getConnectionFactory().getConnection()
                .serverCommands().flushAll();
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
        assertThat(userId).isEqualTo(oauthUser.getId());

        RefreshToken refreshToken = refreshTokenRepository.get(userId);
        assertThat(authTokenResponse.getRefreshToken()).isEqualTo(refreshToken.getToken());
    }

    @Test
    void Refresh_토큰을_재발급받을_수_있다() {
        // given
        OauthUser oauthUser = userConnector.saveOrUpdate(new OauthUser("name", "email", OauthProvider.GOOGLE));

        String accessToken = tokenProvider.createAccessToken(oauthUser);
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);
        refreshTokenRepository.save(oauthUser.getId(), refreshToken);

        // when
        ReissuedAuthTokenResponse reissuedAuthTokenResponse = authService.reissueAuthToken(accessToken, refreshToken.getToken());

        // then
        String reissuedRefreshToken = reissuedAuthTokenResponse.getRefreshToken();
        RefreshToken savedRefreshToken = refreshTokenRepository.get(oauthUser.getId());
        assertThat(reissuedRefreshToken).isEqualTo(savedRefreshToken.getToken());
    }

    @Test
    void Refresh_토큰이_유효하지_않으면_유저의_Refresh_토큰이_삭제되고_에러가_발생한다() {
        // given
        OauthUser oauthUser = userConnector.saveOrUpdate(new OauthUser("name", "email", OauthProvider.GOOGLE));
        Long oauthUserId = oauthUser.getId();
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);
        refreshTokenRepository.save(oauthUserId, refreshToken);

        String accessToken = tokenProvider.createAccessToken(oauthUser);
        String otherRefreshToken = tokenProvider.createRefreshToken(oauthUser).getToken();

        // when then
        assertThatThrownBy(() -> authService.reissueAuthToken(accessToken, otherRefreshToken))
                .isInstanceOf(InvalidRefreshTokenException.class);
        assertThatThrownBy(() -> refreshTokenRepository.get(oauthUserId))
                .isInstanceOf(RefreshTokenNotFoundException.class);
    }

    @Test
    void 존재하지_않는_Refresh_토큰을_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> refreshTokenRepository.get(noneExistId))
                .isInstanceOf(RefreshTokenNotFoundException.class);
    }

    @Test
    void 존재하지_않는_유저를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> userConnector.findById(noneExistId))
                .isInstanceOf(OauthUserNotFoundException.class);
    }

}

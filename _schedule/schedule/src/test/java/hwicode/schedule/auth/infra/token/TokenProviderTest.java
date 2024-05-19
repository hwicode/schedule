package hwicode.schedule.auth.infra.token;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.infra.token.InvalidTokenException;
import hwicode.schedule.auth.exception.infra.token.InvalidOauthUserException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenProviderTest {

    private TokenProvider createTokenProvider() {
        return new TokenProvider("issuer", 10000, 10000, "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
    }

    @Test
    void Access_토큰을_만들_수_있다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        // when
        String accessToken = tokenProvider.createAccessToken(oauthUser);

        // then
        DecodedToken decodedToken = tokenProvider.decodeToken(accessToken);
        assertThat(decodedToken.getUserId()).isEqualTo(oauthUserId);
    }

    @Test
    void Refresh_토큰을_만들_수_있다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        // when
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);

        // then
        DecodedToken decodedToken = tokenProvider.decodeToken(refreshToken.getToken());
        assertThat(decodedToken.getUserId()).isEqualTo(oauthUserId);
    }

    @Test
    void Refresh_토큰을_재발급할_수_있고_이전의_Refresh_token과_유효_기간이_같아야_한다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);

        // when
        RefreshToken reissuedRefreshToken = tokenProvider.reissueRefreshToken(oauthUser, refreshToken.getToken());

        // then
        DecodedToken decodedToken = tokenProvider.decodeToken(reissuedRefreshToken.getToken());
        assertThat(decodedToken.getUserId()).isEqualTo(oauthUserId);
        assertThat(reissuedRefreshToken.getExpiryMs()).isLessThan(refreshToken.getExpiryMs());
    }

    @Test
    void 토큰을_생성_시_유효한_유저가_아닌_경우_에러가_발생한다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        OauthUser oauthUser = new OauthUser(null, null, null, null);

        // when
        assertThatThrownBy(() -> tokenProvider.createAccessToken(oauthUser))
                .isInstanceOf(InvalidOauthUserException.class);
    }

    @Test
    void 토큰을_디코드할_수_있다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        String accessToken = tokenProvider.createAccessToken(oauthUser);

        // when
        DecodedToken decodedToken = tokenProvider.decodeToken(accessToken);

        // then
        assertThat(decodedToken.getUserId()).isEqualTo(oauthUserId);
    }

    @Test
    void 만료된_토큰을_디코드하면_에러가_발생한다() {
        // given
        TokenProvider tokenProvider = new TokenProvider("issuer", 0, 0, "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        String accessToken = tokenProvider.createAccessToken(oauthUser);

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeToken(accessToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"iefnda", "dsin.ddw.dfsf", "funeewqsada.fdsfccc", "", "Bearer dvdsaimismfaksmfesfmaiefm"})
    void 토큰_디코드시에_jwt가_아니라면_에러가_발생한다(String token) {
        // given
        TokenProvider tokenProvider = createTokenProvider();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 토큰_디코드시에_null이_인자로_들어오면_에러가_발생한다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeToken(null))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 토큰_디코드시에_발행자가_다르면_에러가_발생한다() {
        // given
        String secretKey = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";
        TokenProvider tokenProvider = new TokenProvider("issuer", 100000, 100000, secretKey);

        Long userId = 1L;
        Date now = new Date();
        String token = Jwts.builder()
                .setIssuer("aaa")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 100000))
                .claim("userId", userId)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 토큰을_디코드할_때_유저_id가_존재하지_않으면_에러가_발생한다() {
        // given
        String secretKey = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";
        TokenProvider tokenProvider = new TokenProvider("issuer", 100000, 100000, secretKey);

        Date now = new Date();
        String token = Jwts.builder()
                .setIssuer("issuer")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 100000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeToken(token))
                .isInstanceOf(InvalidOauthUserException.class);
    }

}

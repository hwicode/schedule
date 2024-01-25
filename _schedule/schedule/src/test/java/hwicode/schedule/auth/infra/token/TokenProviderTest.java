package hwicode.schedule.auth.infra.token;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.infra.token.InvalidAccessTokenException;
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
        DecodedAccessToken decodedAccessToken = tokenProvider.decodeAccessToken(accessToken);
        assertThat(decodedAccessToken.getUserId()).isEqualTo(oauthUserId);
    }

    @Test
    void Refresh_토큰을_만들_수_있다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        // when
        RefreshToken refreshToken = tokenProvider.createRefreshToken(oauthUser);
        RefreshToken refreshToken2 = tokenProvider.createRefreshToken(oauthUser);

        // then
        String token = refreshToken.getToken();
        String token2 = refreshToken2.getToken();
        assertThat(token).isNotEmpty();
        assertThat(token2).isNotEmpty();
        assertThat(token).isNotEqualTo(token2);
    }

    @Test
    void 유효한_유저가_아닌_경우_에러가_발생한다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();
        OauthUser oauthUser = new OauthUser(null, null, null, null);

        // when
        assertThatThrownBy(() -> tokenProvider.createAccessToken(oauthUser))
                .isInstanceOf(InvalidOauthUserException.class);
    }

    @Test
    void decodeAccessToken_메서드를_통해_만료된_Access_토큰을_디코드하면_에러가_발생한다() {
        // given
        TokenProvider tokenProvider = new TokenProvider("issuer", 0, 0, "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        String accessToken = tokenProvider.createAccessToken(oauthUser);

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeAccessToken(accessToken))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void decodeExpiredAccessToken_메서드를_통해_만료된_Access_토큰을_디코드할_수_있다() {
        // given
        TokenProvider tokenProvider = new TokenProvider("issuer", 0, 0, "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        Long oauthUserId = 1L;
        OauthUser oauthUser = new OauthUser(oauthUserId, null, null, null);

        String accessToken = tokenProvider.createAccessToken(oauthUser);

        // when
        DecodedAccessToken decodedAccessToken = tokenProvider.decodeExpiredAccessToken(accessToken);

        // then
        assertThat(decodedAccessToken.getUserId()).isEqualTo(oauthUserId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"iefnda", "dsin.ddw.dfsf", "funeewqsada.fdsfccc", ""})
    void Access_토큰_디코드시에_jwt가_아니라면_에러가_발생한다(String token) {
        // given
        TokenProvider tokenProvider = createTokenProvider();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeAccessToken(token))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void Access_토큰_디코드시에_null이_인자로_들어오면_에러가_발생한다() {
        // given
        TokenProvider tokenProvider = createTokenProvider();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeAccessToken(null))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void Access_토큰_디코드시에_발행자가_다르면_에러가_발생한다() {
        // given
        String secretKey = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";
        TokenProvider tokenProvider = new TokenProvider("issuer", 1000, 1000, secretKey);

        Long userId = 1L;
        Date now = new Date();
        String token = Jwts.builder()
                .setIssuer("aaa")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 1000))
                .claim("userId", userId)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        // when then
        assertThatThrownBy(() -> tokenProvider.decodeAccessToken(token))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

}

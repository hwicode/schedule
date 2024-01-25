package hwicode.schedule.auth.infra.token;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.domain.RefreshToken;
import hwicode.schedule.auth.exception.infra.OauthUserNotValidException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenProviderTest {

    private TokenProvider createTokenProvider() {
        return new TokenProvider("issuer", 10000, 10000, "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
    }

    @Test
    void 액세스_토큰을_만들_수_있다() {
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
    void 리플레시_토큰을_만들_수_있다() {
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
                .isInstanceOf(OauthUserNotValidException.class);
    }

}

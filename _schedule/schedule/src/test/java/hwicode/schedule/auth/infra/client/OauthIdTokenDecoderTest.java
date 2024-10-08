package hwicode.schedule.auth.infra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.exception.infra.client.IdTokenClaimException;
import hwicode.schedule.auth.exception.infra.client.InvalidIdTokenException;
import hwicode.schedule.auth.exception.infra.client.OauthIdTokenException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OauthIdTokenDecoderTest {

    @Test
    void id토큰을_디코드할_수_있다() {
        // given
        String name = "name";
        String email = "email";
        String jwt = Jwts.builder()
                .claim(name, name)
                .claim(email, email)
                .compact();

        OauthIdTokenDecoder oauthIdTokenDecoder = new OauthIdTokenDecoder(new ObjectMapper());

        // when
        Map<String, String> decode = oauthIdTokenDecoder.decode(jwt);

        // then
        assertThat(decode)
                .containsEntry(name, name)
                .containsEntry(email, email);
    }

    @Test
    void 잘못된_형식의_id토큰을_디코드하면_에러가_발생한다() {
        // given
        String jwt = "dfim.idf";
        OauthIdTokenDecoder oauthIdTokenDecoder = new OauthIdTokenDecoder(new ObjectMapper());

        // when then
        assertThatThrownBy(() -> oauthIdTokenDecoder.decode(jwt))
                .isInstanceOf(OauthIdTokenException.class);
    }

    @Test
    void null값을_디코드하면_에러가_발생한다() {
        // given
        OauthIdTokenDecoder oauthIdTokenDecoder = new OauthIdTokenDecoder(new ObjectMapper());

        // when then
        assertThatThrownBy(() -> oauthIdTokenDecoder.decode(null))
                .isInstanceOf(InvalidIdTokenException.class);
    }

    @Test
    void OAuth2_프로바이더로부터_가져온_유저_정보에_이름이_존재하지_않으면_에러가_발생한다() {
        // given
        OauthIdTokenDecoder oauthIdTokenDecoder = new OauthIdTokenDecoder(new ObjectMapper());

        Map<String, String> oauthUserInfo = new HashMap<>();
        oauthUserInfo.put("email", "email");

        // when then
        assertThatThrownBy(() -> oauthIdTokenDecoder.validateClaim(oauthUserInfo, "name"))
                .isInstanceOf(IdTokenClaimException.class);
    }

    @Test
    void OAuth2_프로바이더로부터_가져온_유저_정보에_이메일이_존재하지_않으면_에러가_발생한다() {
        // given
        OauthIdTokenDecoder oauthIdTokenDecoder = new OauthIdTokenDecoder(new ObjectMapper());

        Map<String, String> oauthUserInfo = new HashMap<>();
        oauthUserInfo.put("name", "name");

        // when then
        assertThatThrownBy(() -> oauthIdTokenDecoder.validateClaim(oauthUserInfo, "email"))
                .isInstanceOf(IdTokenClaimException.class);
    }

}

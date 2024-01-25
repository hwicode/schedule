package hwicode.schedule.auth.infra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.exception.infra.OauthIdTokenException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

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
    void 잘못된_id토큰을_디코드하면_에러가_발생한다() {
        // given
        String jwt = "dfim.idf";
        OauthIdTokenDecoder oauthIdTokenDecoder = new OauthIdTokenDecoder(new ObjectMapper());

        // when then
        assertThatThrownBy(() -> oauthIdTokenDecoder.decode(jwt))
                .isInstanceOf(OauthIdTokenException.class);
    }

}

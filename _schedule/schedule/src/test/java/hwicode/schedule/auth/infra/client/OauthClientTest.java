package hwicode.schedule.auth.infra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.application.OauthClient;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.client.google.GoogleFetcher;
import hwicode.schedule.auth.infra.client.google.GoogleOauthClient;
import hwicode.schedule.auth.infra.client.google.GoogleProperties;
import hwicode.schedule.auth.infra.client.google.dto.GoogleTokenResponse;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static hwicode.schedule.auth.AuthDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OauthClientTest {

    @Test
    void 구글의_로그인_페이지_url을_가져올_수_있다() {
        // given
        GoogleProperties googleProperties = new GoogleProperties(
                CLIENT_ID, CLIENT_SECRET, AUTH_URL, RESPONSE_TYPE,
                TOKEN_URL, GRANT_TYPE, REDIRECT_URL, SCOPES);

        OauthClient googleOauthClient = new GoogleOauthClient(googleProperties, new GoogleFetcher(new RestTemplate()), new OauthIdTokenDecoder(new ObjectMapper()));

        // when
        String authUrl = googleOauthClient.getAuthUrl();

        // then
        String expectedUrl = UriComponentsBuilder.fromHttpUrl(AUTH_URL)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", REDIRECT_URL)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("scope", String.join(" ", SCOPES))
                .encode()
                .build().toString();
        assertThat(authUrl).isEqualTo(expectedUrl);
    }

    @Test
    void 구글로부터_유저의_정보를_가져올_수_있다() {
        // given
        GoogleProperties googleProperties = new GoogleProperties(
                CLIENT_ID, CLIENT_SECRET, AUTH_URL, RESPONSE_TYPE,
                TOKEN_URL, GRANT_TYPE, REDIRECT_URL, SCOPES);

        String token = Jwts.builder()
                .claim("name", "name")
                .claim("email", "email")
                .compact();

        GoogleFetcher googleFetcher = mock(GoogleFetcher.class);
        when(googleFetcher.fetchGoogleToken(any(), any()))
                .thenReturn(new GoogleTokenResponse(token));

        OauthClient googleOauthClient = new GoogleOauthClient(googleProperties, googleFetcher, new OauthIdTokenDecoder(new ObjectMapper()));

        // when
        OauthUser oauthUser = googleOauthClient.getUserInfo("code");

        // then
        assertThat(oauthUser.getName()).isEqualTo("name");
        assertThat(oauthUser.getEmail()).isEqualTo("email");
    }

}

package hwicode.schedule.auth.infra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.application.OauthClient;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.infra.client.google.GoogleOauthClient;
import hwicode.schedule.auth.infra.client.google.GoogleProperties;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static hwicode.schedule.auth.AuthDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OauthClientTest {

    @Test
    void 구글의_로그인_페이지_url을_가져올_수_있다() {
        // given
        GoogleProperties googleProperties = mock(GoogleProperties.class);
        when(googleProperties.getAuthenticationUrl()).thenReturn(AUTH_URL);
        when(googleProperties.getClientId()).thenReturn(CLIENT_ID);
        when(googleProperties.getRedirectUri()).thenReturn(REDIRECT_URL);
        when(googleProperties.getResponseType()).thenReturn(RESPONSE_TYPE);
        when(googleProperties.getScopes()).thenReturn(SCOPES);

        OauthClient googleOauthClient = new GoogleOauthClient(googleProperties, new RestTemplate(), new OauthIdTokenDecoder(new ObjectMapper()));

        OauthClientMapper oauthClientMapper = new OauthClientMapper(List.of(googleOauthClient));

        // when
        String authUrl = oauthClientMapper.getAuthUrl(OauthProvider.GOOGLE);

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
}

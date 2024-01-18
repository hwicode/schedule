package hwicode.schedule.auth.infra.client.google;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.OauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class GoogleOauthClient implements OauthClient {

    private final GoogleProperties googleProperties;

    @Override
    public OauthProvider getOauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public String getAuthUrl() {
        return UriComponentsBuilder.fromHttpUrl(googleProperties.getAuthenticationUrl())
                .queryParam("client_id", googleProperties.getClientId())
                .queryParam("redirect_uri", googleProperties.getRedirectUri())
                .queryParam("response_type", googleProperties.getResponseType())
                .queryParam("scope", String.join(" ", googleProperties.getScopes()))
                .build().toString();
    }
}

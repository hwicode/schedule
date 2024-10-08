package hwicode.schedule.auth.infra.client.google;

import hwicode.schedule.auth.application.OauthClient;
import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.client.OauthIdTokenDecoder;
import hwicode.schedule.auth.infra.client.google.dto.GoogleTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class GoogleOauthClient implements OauthClient {

    private final OauthProvider oauthProvider;
    private final GoogleProperties googleProperties;
    private final GoogleFetcher googleFetcher;
    private final OauthIdTokenDecoder oauthIdTokenDecoder;

    public GoogleOauthClient(GoogleProperties googleProperties, GoogleFetcher googleFetcher, OauthIdTokenDecoder oauthIdTokenDecoder) {
        this.oauthProvider = OauthProvider.GOOGLE;
        this.googleProperties = googleProperties;
        this.googleFetcher = googleFetcher;
        this.oauthIdTokenDecoder = oauthIdTokenDecoder;
    }

    @Override
    public OauthProvider getOauthProvider() {
        return this.oauthProvider;
    }

    @Override
    public String getAuthUrl() {
        return UriComponentsBuilder.fromHttpUrl(googleProperties.getAuthenticationUrl())
                .queryParam("client_id", googleProperties.getClientId())
                .queryParam("redirect_uri", googleProperties.getRedirectUri())
                .queryParam("response_type", googleProperties.getResponseType())
                .queryParam("scope", String.join(" ", googleProperties.getScopes()))
                .encode()
                .build().toString();
    }

    @Override
    public OauthUser getUserInfo(String code) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = makeHttpEntity(code);
        GoogleTokenResponse googleTokenResponse = googleFetcher.fetchGoogleToken(googleProperties.getTokenUrl(), httpEntity);

        Map<String, String> oauthUserInfo = oauthIdTokenDecoder.decode(googleTokenResponse.getIdToken());
        return makeOauthUser(oauthUserInfo);
    }

    private HttpEntity<MultiValueMap<String, String>> makeHttpEntity(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = generateBody(code);

        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> generateBody(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        params.add("code", code);
        params.add("grant_type", googleProperties.getGrantType());
        params.add("redirect_uri", googleProperties.getRedirectUri());
        return params;
    }

    private OauthUser makeOauthUser(Map<String, String> oauthUserInfo) {
        String name = oauthIdTokenDecoder.validateClaim(oauthUserInfo, "name");
        String email = oauthIdTokenDecoder.validateClaim(oauthUserInfo, "email");
        return new OauthUser(name, email, this.oauthProvider);
    }
}

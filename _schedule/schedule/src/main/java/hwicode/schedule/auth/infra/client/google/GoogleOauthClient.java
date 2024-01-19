package hwicode.schedule.auth.infra.client.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.OauthClient;
import hwicode.schedule.auth.application.dto.UserInfo;
import hwicode.schedule.auth.exception.infra.OauthIdTokenException;
import hwicode.schedule.auth.exception.infra.OauthServerException;
import hwicode.schedule.auth.infra.client.google.dto.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class GoogleOauthClient implements OauthClient {

    private final GoogleProperties googleProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

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

    @Override
    public UserInfo getUserInfo(String code) {
        GoogleTokenResponse googleTokenResponse = requestGoogleToken(code);
        return getUserInfo(googleTokenResponse);
    }

    private GoogleTokenResponse requestGoogleToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = generateBody(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        return fetchGoogleToken(request);
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

    private GoogleTokenResponse fetchGoogleToken(HttpEntity<MultiValueMap<String, String>> request) {
        try {
            return restTemplate.postForObject(googleProperties.getTokenUrl(), request, GoogleTokenResponse.class);
        } catch (RestClientException e) {
            throw new OauthServerException();
        }
    }

    private UserInfo getUserInfo(GoogleTokenResponse googleTokenResponse) {
        // Jwt token
        String payload = googleTokenResponse.getIdToken().split("\\.")[1];
        String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(decodedPayload, UserInfo.class);
        } catch (final JsonProcessingException e) {
            throw new OauthIdTokenException();
        }
    }
}

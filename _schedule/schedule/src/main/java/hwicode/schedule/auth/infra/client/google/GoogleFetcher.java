package hwicode.schedule.auth.infra.client.google;

import hwicode.schedule.auth.exception.infra.client.OauthServerException;
import hwicode.schedule.auth.infra.client.google.dto.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class GoogleFetcher {

    private final RestTemplate restTemplate;

    public GoogleTokenResponse fetchGoogleToken(String tokenUrl, HttpEntity<MultiValueMap<String, String>> request) {
        try {
            return restTemplate.postForObject(tokenUrl, request, GoogleTokenResponse.class);
        } catch (RestClientException e) {
            throw new OauthServerException(e);
        }
    }

}

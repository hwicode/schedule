package hwicode.schedule.auth.infra.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.exception.infra.client.IdTokenClaimException;
import hwicode.schedule.auth.exception.infra.client.InvalidIdTokenException;
import hwicode.schedule.auth.exception.infra.client.OauthIdTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OauthIdTokenDecoder {

    private final ObjectMapper objectMapper;

    public Map<String, String> decode(String idToken) {
        validateIdToken(idToken);
        String payload = idToken.split("\\.")[1];
        String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(decodedPayload, new TypeReference<>() {});
        } catch (final JsonProcessingException e) {
            throw new OauthIdTokenException();
        }
    }

    private void validateIdToken(String idToken) {
        if (idToken == null) {
            throw new InvalidIdTokenException();
        }
    }

    public String validateClaim(Map<String, String> oauthUserInfo, String key) {
        String value = oauthUserInfo.get(key);
        if (value == null) {
            throw new IdTokenClaimException();
        }
        return value;
    }
}

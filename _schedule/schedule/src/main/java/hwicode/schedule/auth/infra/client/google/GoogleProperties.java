package hwicode.schedule.auth.infra.client.google;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Validated
@RequiredArgsConstructor
@ConfigurationProperties("oauth2.google")
@ConstructorBinding
public class GoogleProperties {

    @NotEmpty
    private final String clientId;
    @NotEmpty
    private final String clientSecret;
    @NotEmpty
    private final String authenticationUrl;
    @NotEmpty
    private final String responseType;
    @NotEmpty
    private final String tokenUrl;
    @NotEmpty
    private final String grantType;
    @NotEmpty
    private final String redirectUri;
    @NotEmpty
    private final List<String> scopes;
}

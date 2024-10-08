package hwicode.schedule.auth.infra.client;

import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.application.OauthClient;
import hwicode.schedule.auth.exception.application.OauthClientNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OauthClientMapper {

    private final Map<OauthProvider, OauthClient> clientMap;

    public OauthClientMapper(List<OauthClient> oauthClients) {
        this.clientMap = oauthClients.stream().collect(
                Collectors.toUnmodifiableMap(OauthClient::getOauthProvider, Function.identity())
        );
    }

    public OauthClient getOauthClient(OauthProvider oauthProvider) {
        return Optional.ofNullable(clientMap.get(oauthProvider))
                .orElseThrow(OauthClientNotFoundException::new);
    }

}

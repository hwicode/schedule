package hwicode.schedule.auth.infra.client;

import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfo {

    private final String name;
    private final String email;
    private final OauthProvider oauthProvider;

    public OauthUser toEntity() {
        return new OauthUser(name, email, oauthProvider);
    }
}

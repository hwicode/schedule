package hwicode.schedule.auth.application;

import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.infra.client.OauthClientMapper;
import hwicode.schedule.auth.infra.client.UserInfo;
import hwicode.schedule.auth.infra.other_boundedcontext.UserConnector;
import hwicode.schedule.auth.infra.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final OauthClientMapper oauthClientMapper;
    private final UserConnector userConnector;
    private final TokenProvider tokenProvider;

    public String getOauthLoginUrl(OauthProvider oauthProvider) {
        return oauthClientMapper.getAuthUrl(oauthProvider);
    }

    public void loginWithOauth(OauthProvider oauthProvider, String code) {
        OauthClient oauthClient = oauthClientMapper.getOauthClient(oauthProvider);
        UserInfo userInfo = oauthClient.getUserInfo(code);

        OauthUser oauthUser = userConnector.saveOrUpdate(userInfo.toEntity());

    }
}

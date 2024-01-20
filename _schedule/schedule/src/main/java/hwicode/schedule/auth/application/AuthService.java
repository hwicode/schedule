package hwicode.schedule.auth.application;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.dto.SavedUserInfo;
import hwicode.schedule.auth.application.dto.UserInfo;
import hwicode.schedule.auth.infra.other_boundedcontext.UserConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final OauthClientMapper oauthClientMapper;
    private final UserConnector userConnector;

    public String getOauthLoginUrl(OauthProvider oauthProvider) {
        return oauthClientMapper.getAuthUrl(oauthProvider);
    }

    public void loginWithOauth(OauthProvider oauthProvider, String code) {
        OauthClient oauthClient = oauthClientMapper.getOauthClient(oauthProvider);
        UserInfo userInfo = oauthClient.getUserInfo(code);

        SavedUserInfo savedUserInfo = userConnector.createOrUpdate(userInfo);

    }
}

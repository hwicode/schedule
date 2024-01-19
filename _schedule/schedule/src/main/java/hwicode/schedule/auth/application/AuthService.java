package hwicode.schedule.auth.application;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final OauthClientMapper oauthClientMapper;

    public String getOauthLoginUrl(OauthProvider oauthProvider) {
        return oauthClientMapper.getAuthUrl(oauthProvider);
    }

    public void loginWithOauth(OauthProvider oauthProvider, String code) {
        OauthClient oauthClient = oauthClientMapper.getOauthClient(oauthProvider);
        UserInfo userInfo = oauthClient.getUserInfo(code);
    }
}

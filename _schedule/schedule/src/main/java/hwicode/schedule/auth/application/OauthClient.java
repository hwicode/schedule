package hwicode.schedule.auth.application;

import hwicode.schedule.auth.OauthProvider;
import hwicode.schedule.auth.application.dto.UserInfo;

public interface OauthClient {

    OauthProvider getOauthProvider();
    String getAuthUrl();
    UserInfo getUserInfo(String code);
}

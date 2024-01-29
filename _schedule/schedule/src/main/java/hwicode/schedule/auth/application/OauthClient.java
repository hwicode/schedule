package hwicode.schedule.auth.application;

import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.domain.OauthUser;

public interface OauthClient {

    OauthProvider getOauthProvider();
    String getAuthUrl();
    OauthUser getUserInfo(String code);
}

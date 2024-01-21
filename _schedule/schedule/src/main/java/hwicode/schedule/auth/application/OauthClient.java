package hwicode.schedule.auth.application;

import hwicode.schedule.auth.domain.OauthProvider;
import hwicode.schedule.auth.infra.client.UserInfo;

public interface OauthClient {

    OauthProvider getOauthProvider();
    String getAuthUrl();
    UserInfo getUserInfo(String code);
}

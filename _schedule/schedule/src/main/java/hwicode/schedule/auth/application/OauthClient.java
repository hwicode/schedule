package hwicode.schedule.auth.application;

import hwicode.schedule.auth.OauthProvider;

public interface OauthClient {

    OauthProvider getOauthProvider();
    String getAuthUrl();
}

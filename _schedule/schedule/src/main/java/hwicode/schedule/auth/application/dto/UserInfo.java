package hwicode.schedule.auth.application.dto;

import hwicode.schedule.auth.OauthProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfo {

    private final String name;
    private final String email;
    private final OauthProvider oauthProvider;
}

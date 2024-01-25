package hwicode.schedule.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthUser {

    private Long id;
    private String name;
    private String email;
    private OauthProvider oauthProvider;

    public OauthUser(String name, String email, OauthProvider oauthProvider) {
        this.name = name;
        this.email = email;
        this.oauthProvider = oauthProvider;
    }
}

package hwicode.schedule.auth.domain;

import hwicode.schedule.auth.OauthProvider;
import lombok.AllArgsConstructor;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public OauthProvider getOauthProvider() {
        return oauthProvider;
    }
}

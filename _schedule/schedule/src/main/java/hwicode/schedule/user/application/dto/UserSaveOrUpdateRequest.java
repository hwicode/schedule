package hwicode.schedule.user.application.dto;

import hwicode.schedule.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSaveOrUpdateRequest {

    private String name;
    private String email;
    private String oauthProvider;

    public User toEntity() {
        return new User(name, email, oauthProvider);
    }
}

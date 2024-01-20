package hwicode.schedule.auth.infra.other_boundedcontext;

import hwicode.schedule.auth.application.dto.SavedUserInfo;
import hwicode.schedule.auth.application.dto.UserInfo;
import hwicode.schedule.user.application.UserService;
import hwicode.schedule.user.application.dto.UserSaveOrUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserConnector {

    private final UserService userService;

    public SavedUserInfo createOrUpdate(UserInfo userInfo) {
        UserSaveOrUpdateRequest userSaveOrUpdateRequest = new UserSaveOrUpdateRequest(userInfo.getName(), userInfo.getEmail(), userInfo.getOauthProvider().name());
        Long userId = userService.createOrUpdate(userSaveOrUpdateRequest);
        return new SavedUserInfo(userId);
    }

}

package hwicode.schedule.auth.infra.other_boundedcontext;

import hwicode.schedule.auth.domain.OauthUser;
import hwicode.schedule.auth.exception.infra.other_boundedcontext.OauthUserNotFoundException;
import hwicode.schedule.auth.infra.jpa_repository.OauthUserRepository;
import hwicode.schedule.user.application.UserService;
import hwicode.schedule.user.application.dto.UserSaveOrUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserConnector {

    private final UserService userService;
    private final OauthUserRepository oauthUserRepository;

    public OauthUser saveOrUpdate(OauthUser oauthUser) {
        UserSaveOrUpdateRequest userSaveOrUpdateRequest = new UserSaveOrUpdateRequest(
                oauthUser.getName(), oauthUser.getEmail(), oauthUser.getOauthProvider().name()
        );

        Long userId = userService.saveOrUpdate(userSaveOrUpdateRequest);
        return new OauthUser(userId, oauthUser.getName(), oauthUser.getEmail(), oauthUser.getOauthProvider());
    }

    public OauthUser findById(Long id) {
        return oauthUserRepository.findById(id)
                .orElseThrow(OauthUserNotFoundException::new);
    }

}

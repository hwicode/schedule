package hwicode.schedule.user.application;

import hwicode.schedule.user.application.dto.UserSaveOrUpdateRequest;
import hwicode.schedule.user.domain.User;
import hwicode.schedule.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long saveOrUpdate(UserSaveOrUpdateRequest userSaveOrUpdateRequest) {
        User user = userRepository.findByEmail(userSaveOrUpdateRequest.getEmail())
                .orElseGet(() -> userRepository.save(userSaveOrUpdateRequest.toEntity()));

        user.update(userSaveOrUpdateRequest.getName());
        return user.getId();
    }
}

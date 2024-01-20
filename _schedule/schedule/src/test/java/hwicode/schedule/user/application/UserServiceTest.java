package hwicode.schedule.user.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.user.application.dto.UserSaveOrUpdateRequest;
import hwicode.schedule.user.domain.User;
import hwicode.schedule.user.infra.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 유저의_이름과_이메일을_변경할_수_있다() {
        // given
        String name = "name";
        String email = "email";
        String oauthProvider = "oauthProvider";

        User user = new User(name, email, oauthProvider);
        userRepository.save(user);

        String newName = "newName";
        UserSaveOrUpdateRequest userSaveOrUpdateRequest = new UserSaveOrUpdateRequest(newName, email, oauthProvider);

        // when
        userService.createOrUpdate(userSaveOrUpdateRequest);

        // then
        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo(newName);
    }

}

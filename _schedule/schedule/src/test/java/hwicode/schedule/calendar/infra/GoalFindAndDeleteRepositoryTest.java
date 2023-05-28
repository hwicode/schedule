package hwicode.schedule.calendar.infra;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.exception.GoalNotFoundException;
import hwicode.schedule.calendar.infra.limited_repository.GoalFindAndDeleteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class GoalFindAndDeleteRepositoryTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    GoalFindAndDeleteRepository goalFindAndDeleteRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 존재하지_않는_목표를_가져오면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // then when
        assertThatThrownBy(() -> goalFindAndDeleteRepository.findGoalWithSubGoals(noneExistId))
                .isInstanceOf(GoalNotFoundException.class);
    }
}

package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.calendar.CalendarDataHelper.GOAL_NAME;
import static hwicode.schedule.calendar.CalendarDataHelper.SUB_GOAL_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GoalAggregateServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    GoalAggregateService goalAggregateService;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    SubGoalRepository subGoalRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 목표에_서브_목표를_추가할_수_있다() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        goalRepository.save(goal);

        // when
        Long subGoalId = goalAggregateService.createSubGoal(goal.getId(), SUB_GOAL_NAME);

        // then
        assertThat(subGoalRepository.existsById(subGoalId)).isTrue();
    }

}

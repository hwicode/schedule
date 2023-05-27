package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoalStatus;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotAllDoneException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    private Goal createGoalWithSubGoal(String subGoalName) {
        Goal goal = new Goal(GOAL_NAME);
        goal.createSubGoal(subGoalName);
        return goal;
    }

    @Test
    void 목표에서_서브_목표를_추가할_수_있다() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        goalRepository.save(goal);

        // when
        Long subGoalId = goalAggregateService.createSubGoal(goal.getId(), SUB_GOAL_NAME);

        // then
        assertThat(subGoalRepository.existsById(subGoalId)).isTrue();
    }

    @Test
    void 목표에서_서브_목표의_이름을_변경할_수_있다() {
        // given
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME);
        goalRepository.save(goal);

        // when
        goalAggregateService.changeSubGoalName(goal.getId(), SUB_GOAL_NAME, SUB_GOAL_NAME2);

        // then
        Goal savedGoal = goalRepository.findGoalWithSubGoals(goal.getId()).orElseThrow();
        assertThatThrownBy(() -> savedGoal.createSubGoal(SUB_GOAL_NAME2))
                .isInstanceOf(SubGoalDuplicateException.class);
    }

    @Test
    void 목표에서_서브_목표를_제거할_수_있다() {
        // given
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME);
        goalRepository.save(goal);

        // when
        goalAggregateService.deleteSubGoal(goal.getId(), SUB_GOAL_NAME);

        // then
        Goal savedGoal = goalRepository.findGoalWithSubGoals(goal.getId()).orElseThrow();
        assertThatThrownBy(() -> savedGoal.deleteSubGoal(SUB_GOAL_NAME))
                .isInstanceOf(SubGoalNotFoundException.class);
    }

    @Test
    void 목표에서_서브_목표의_진행_상태를_변경할_수_있다() {
        // given
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goalRepository.save(goal);

        // when
        goalAggregateService.changeSubGoalStatus(goal.getId(), SUB_GOAL_NAME, SubGoalStatus.TODO);

        // then
        Goal savedGoal = goalRepository.findGoalWithSubGoals(goal.getId()).orElseThrow();
        assertThatThrownBy(() -> savedGoal.changeGoalStatus(GoalStatus.DONE))
                .isInstanceOf(SubGoalNotAllDoneException.class);
    }

}

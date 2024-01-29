package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.dto.calendar.GoalAddToCalendersCommand;
import hwicode.schedule.calendar.domain.*;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotAllDoneException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.goal.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    CalendarService calendarService;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    SubGoalRepository subGoalRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarGoalRepository calendarGoalRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private Goal createGoalWithSubGoal(String subGoalName, Long userId) {
        Goal goal = new Goal(GOAL_NAME, userId);
        goal.createSubGoal(subGoalName);
        return goal;
    }

    @Test
    void 목표에서_서브_목표를_추가할_수_있다() {
        // given
        Long userId = 1L;
        Goal goal = new Goal(GOAL_NAME, userId);
        goalRepository.save(goal);

        // when
        Long subGoalId = goalAggregateService.saveSubGoal(goal.getId(), SUB_GOAL_NAME);

        // then
        assertThat(subGoalRepository.existsById(subGoalId)).isTrue();
    }

    @Test
    void 목표에서_서브_목표의_이름을_변경할_수_있다() {
        // given
        Long userId = 1L;
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME, userId);
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
        Long userId = 1L;
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME, userId);
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
        Long userId = 1L;
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME, userId);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goalRepository.save(goal);

        // when
        goalAggregateService.changeSubGoalStatus(goal.getId(), SUB_GOAL_NAME, SubGoalStatus.TODO);

        // then
        Goal savedGoal = goalRepository.findGoalWithSubGoals(goal.getId()).orElseThrow();
        assertThatThrownBy(() -> savedGoal.changeGoalStatus(GoalStatus.DONE))
                .isInstanceOf(SubGoalNotAllDoneException.class);
    }

    @Test
    void 목표는_진행_상태를_변경할_수_있다() {
        // given
        Long userId = 1L;
        Goal goal = new Goal(GOAL_NAME, userId);
        goalRepository.save(goal);

        // when
        goalAggregateService.changeGoalStatus(goal.getId(), GoalStatus.PROGRESS);

        // then
        Goal savedGoal = goalRepository.findById(goal.getId()).orElseThrow();
        assertThat(savedGoal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표를_삭제하면_목표와_연관된_서브_목표와_캘린더에_연관된_목표도_전부_삭제된다() {
        // given
        Long userId = 1L;
        Calendar calendar = new Calendar(YEAR_MONTH, userId);
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME, userId);

        calendarRepository.save(calendar);
        goalRepository.save(goal);

        GoalAddToCalendersCommand command = new GoalAddToCalendersCommand(userId, goal.getId(), List.of(YEAR_MONTH));
        calendarService.addGoalToCalendars(command);

        // when
        goalAggregateService.deleteGoal(goal.getId());

        // then
        assertThat(goalRepository.existsById(goal.getId())).isFalse();
        assertThat(calendarGoalRepository.findAll()).isEmpty();
        assertThat(subGoalRepository.findAll()).isEmpty();
    }

}

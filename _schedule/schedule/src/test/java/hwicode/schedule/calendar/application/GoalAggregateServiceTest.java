package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.domain.*;
import hwicode.schedule.calendar.exception.GoalNotFoundException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotAllDoneException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
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

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarGoalRepository calendarGoalRepository;

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

    @Test
    void 목표는_진행_상태를_변경할_수_있다() {
        // given
        Goal goal = new Goal(GOAL_NAME);
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
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = createGoalWithSubGoal(SUB_GOAL_NAME);
        CalendarGoal calendarGoal = calendar.addGoal(goal);

        calendarRepository.save(calendar);
        goalRepository.save(goal);
        calendarGoalRepository.save(calendarGoal);

        // when
        goalAggregateService.deleteGoal(goal.getId());

        // then
        assertThat(goalRepository.existsById(goal.getId())).isFalse();
        assertThat(calendarGoalRepository.findAll()).isEmpty();
        assertThat(subGoalRepository.findAll()).isEmpty();
    }

    @Test
    void 목표에서_캘린더와_관련된_목표를_삭제할_수_있다() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        Calendar calendar2 = new Calendar(YEAR_MONTH.plusMonths(1));
        Goal goal = new Goal(GOAL_NAME);
        CalendarGoal calendarGoal = calendar.addGoal(goal);
        CalendarGoal calendarGoal2 = calendar2.addGoal(goal);

        calendarRepository.save(calendar);
        calendarRepository.save(calendar2);
        goalRepository.save(goal);
        calendarGoalRepository.save(calendarGoal);
        calendarGoalRepository.save(calendarGoal2);

        // when
        goalAggregateService.deleteCalendarGoal(goal.getId(), YEAR_MONTH);

        // then
        assertThat(goalRepository.existsById(goal.getId())).isTrue();
        assertThat(calendarGoalRepository.findAll()).hasSize(1);
        assertThat(calendarRepository.findAll()).hasSize(2);
    }

    @Test
    void 목표에서_캘린더와_관련된_목표를_삭제한_후에_목표와_관련된_캘린더가_존재하지_않으면_목표를_삭제한다() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        CalendarGoal calendarGoal = calendar.addGoal(goal);

        calendarRepository.save(calendar);
        goalRepository.save(goal);
        calendarGoalRepository.save(calendarGoal);

        // when
        goalAggregateService.deleteCalendarGoal(goal.getId(), YEAR_MONTH);

        // then
        assertThat(goalRepository.existsById(goal.getId())).isFalse();
        assertThat(calendarGoalRepository.findAll()).isEmpty();
        assertThat(calendarRepository.findAll()).hasSize(1);
    }

    @Test
    void 존재하지_않는_목표를_조회하면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> goalAggregateService.createSubGoal(noneExistId, SUB_GOAL_NAME))
                .isInstanceOf(GoalNotFoundException.class);
    }

}

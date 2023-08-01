package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.domain.CalendarGoalNotFoundException;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarGoalDomainServiceTest {

    @Test
    void 캘린더에_목표를_추가할_수_있다() {
        // given
        CalendarGoalDomainService calendarGoalDomainService = new CalendarGoalDomainService();

        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        List<CalendarGoal> calendarGoals = new ArrayList<>();

        // when
        calendarGoalDomainService.addGoal(calendar, goal, calendarGoals);

        // then
        assertThatThrownBy(() -> calendarGoalDomainService.addGoal(calendar, goal, calendarGoals))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더에_목표를_추가할_때_이미_존재하는_이름이면_에러가_발생한다() {
        // given
        CalendarGoalDomainService calendarGoalDomainService = new CalendarGoalDomainService();
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        List<CalendarGoal> calendarGoals = new ArrayList<>();

        calendarGoalDomainService.addGoal(calendar, goal, calendarGoals);

        // when then
        assertThatThrownBy(() -> calendarGoalDomainService.addGoal(calendar, goal, calendarGoals))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_수_있다() {
        // given
        CalendarGoalDomainService calendarGoalDomainService = new CalendarGoalDomainService();
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        List<CalendarGoal> calendarGoals = new ArrayList<>();

        calendarGoalDomainService.addGoal(calendar, goal, calendarGoals);

        // when
        String changedGoalName = calendarGoalDomainService.changeGoalName(GOAL_NAME, GOAL_NAME2, calendarGoals);

        // then
        Goal duplicatedGoal = new Goal(changedGoalName);
        assertThatThrownBy(() -> calendarGoalDomainService.addGoal(calendar, duplicatedGoal, calendarGoals))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더에_존재하지_않는_목표의_이름_변경을_요청시_에러가_발생한다() {
        // given
        CalendarGoalDomainService calendarGoalDomainService = new CalendarGoalDomainService();
        List<CalendarGoal> calendarGoals = new ArrayList<>();

        // when then
        assertThatThrownBy(() -> calendarGoalDomainService.changeGoalName(GOAL_NAME, GOAL_NAME2, calendarGoals))
                .isInstanceOf(CalendarGoalNotFoundException.class);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_때_이미_존재하는_이름이면_에러가_발생한다() {
        // given
        CalendarGoalDomainService calendarGoalDomainService = new CalendarGoalDomainService();
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        Goal goal2 = new Goal(GOAL_NAME2);
        List<CalendarGoal> calendarGoals = new ArrayList<>();

        calendarGoalDomainService.addGoal(calendar, goal, calendarGoals);
        calendarGoalDomainService.addGoal(calendar, goal2, calendarGoals);

        // when then
        assertThatThrownBy(() -> calendarGoalDomainService.changeGoalName(GOAL_NAME, GOAL_NAME2, calendarGoals))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

}

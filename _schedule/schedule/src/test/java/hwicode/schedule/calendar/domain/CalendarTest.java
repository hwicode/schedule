package hwicode.schedule.calendar.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.GOAL_NAME;
import static hwicode.schedule.calendar.CalendarDataHelper.GOAL_NAME2;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarTest {

    @Test
    void 캘린더는_목표의_이름을_변경할_수_있다() {
        // given
        Calendar calendar = new Calendar();
        Goal goal = new Goal(GOAL_NAME);
        calendar.addGoal(goal);

        // when
        String changedGoalName = calendar.changeGoalName(GOAL_NAME, GOAL_NAME2);

        // then
        Goal duplicatedGoal = new Goal(changedGoalName);
        assertThatThrownBy(() -> calendar.addGoal(duplicatedGoal))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_때_이미_존재하는_이름이면_에러가_발생한다() {
        // given
        Calendar calendar = new Calendar();
        Goal goal = new Goal(GOAL_NAME);
        Goal goal2 = new Goal(GOAL_NAME2);
        calendar.addGoal(goal);
        calendar.addGoal(goal2);

        // when then
        assertThatThrownBy(() -> calendar.changeGoalName(GOAL_NAME, GOAL_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

class Calendar {

    private final List<CalendarGoal> calendarGoals = new ArrayList<>();

    public CalendarGoal addGoal(Goal goal) {
        validateCalendarGoal(goal.getName());
        CalendarGoal calendarGoal = new CalendarGoal(this, goal);
        calendarGoals.add(calendarGoal);
        return calendarGoal;
    }

    public String changeGoalName(String goalName, String newGoalName) {
        validateCalendarGoal(newGoalName);
        return findCalendarGoalBy(goalName).changeGoalName(newGoalName);
    }

    private void validateCalendarGoal(String name) {
        boolean duplication = calendarGoals.stream()
                .anyMatch(calendarGoal -> calendarGoal.isSameGoal(name));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    private CalendarGoal findCalendarGoalBy(String name) {
        return calendarGoals.stream()
                .filter(calendarGoal -> calendarGoal.isSameGoal(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

class CalendarGoal {

    private Calendar calendar;
    private Goal goal;

    CalendarGoal(Calendar calendar, Goal goal) {
        this.calendar = calendar;
        this.goal = goal;
    }

    String changeGoalName(String name) {
        return goal.changeName(name);
    }

    boolean isSameGoal(String name) {
        return goal.isSame(name);
    }
}

package hwicode.schedule.calendar.domain;

import org.junit.jupiter.api.Test;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarTest {

    @Test
    void 캘린더는_목표의_이름을_변경할_수_있다() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
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
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        Goal goal2 = new Goal(GOAL_NAME2);
        calendar.addGoal(goal);
        calendar.addGoal(goal2);

        // when then
        assertThatThrownBy(() -> calendar.changeGoalName(GOAL_NAME, GOAL_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

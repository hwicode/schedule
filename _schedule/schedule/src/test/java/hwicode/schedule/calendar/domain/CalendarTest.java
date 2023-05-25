package hwicode.schedule.calendar.domain;

import hwicode.schedule.calendar.exception.domain.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.WeeklyDateNotValidException;
import org.junit.jupiter.api.Test;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarTest {

    private Calendar createCalendar() {
        return new Calendar(YEAR_MONTH);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_수_있다() {
        // given
        Calendar calendar = createCalendar();
        Goal goal = new Goal(GOAL_NAME);
        calendar.addGoal(goal);

        // when
        String changedGoalName = calendar.changeGoalName(GOAL_NAME, GOAL_NAME2);

        // then
        Goal duplicatedGoal = new Goal(changedGoalName);
        assertThatThrownBy(() -> calendar.addGoal(duplicatedGoal))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_때_이미_존재하는_이름이면_에러가_발생한다() {
        // given
        Calendar calendar = createCalendar();
        Goal goal = new Goal(GOAL_NAME);
        Goal goal2 = new Goal(GOAL_NAME2);
        calendar.addGoal(goal);
        calendar.addGoal(goal2);

        // when then
        assertThatThrownBy(() -> calendar.changeGoalName(GOAL_NAME, GOAL_NAME2))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더에서_일주일동안_공부한_날짜는_0부터_7까지가_될_수_있다() {
        // given
        Calendar calendar = createCalendar();

        for (int i = 0; i < 8; i++) {
            // when
            boolean isChanged = calendar.changeWeeklyStudyDate(i);

            // then
            assertThat(isChanged).isTrue();
        }
    }

    @Test
    void 캘린더에서_일주일동안_공부한_날짜가_7보다_크면_에러가_발생한다() {
        // given
        Calendar calendar = createCalendar();

        // when
        int notValidDate = 8;
        assertThatThrownBy(() -> calendar.changeWeeklyStudyDate(notValidDate))
                .isInstanceOf(WeeklyDateNotValidException.class);
    }

    @Test
    void 캘린더에서_일주일동안_공부한_날짜가_0보다_작으면_에러가_발생한다() {
        // given
        Calendar calendar = createCalendar();

        // when
        int notValidDate = -5;
        assertThatThrownBy(() -> calendar.changeWeeklyStudyDate(notValidDate))
                .isInstanceOf(WeeklyDateNotValidException.class);
    }
}

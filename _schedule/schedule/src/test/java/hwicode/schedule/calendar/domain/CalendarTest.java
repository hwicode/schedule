package hwicode.schedule.calendar.domain;

import hwicode.schedule.calendar.exception.domain.calendar.WeeklyDateNotValidException;
import org.junit.jupiter.api.Test;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalendarTest {

    private Calendar createCalendar() {
        return new Calendar(YEAR_MONTH, 1L);
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

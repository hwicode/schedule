package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CalendarServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    CalendarService calendarService;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    CalendarGoalRepository calendarGoalRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 기간을_정하고_목표를_추가하면_기간만큼_목표가_캘린더에_추가된다() {
        // given
        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2)
        );

        // when
        Long goalId = calendarService.saveGoal(GOAL_NAME, yearMonths);

        // then
        assertThat(goalRepository.existsById(goalId)).isTrue();
        assertThat(calendarGoalRepository.findAll()).hasSize(3);
    }

    @Test
    void 목표를_일정_기간동안_연장할_수_있다() {
        // given
        Long goalId = calendarService.saveGoal(GOAL_NAME, List.of(YEAR_MONTH));

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2),
                YEAR_MONTH.plusMonths(3)
        );

        // when
        calendarService.addGoalToCalendars(goalId, yearMonths);

        // then
        assertThat(goalRepository.findAll()).hasSize(1);
        assertThat(goalRepository.existsById(goalId)).isTrue();
        assertThat(calendarGoalRepository.findAll()).hasSize(4);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_수_있다() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        CalendarGoal calendarGoal = calendar.addGoal(goal);

        calendarRepository.save(calendar);
        goalRepository.save(goal);
        calendarGoalRepository.save(calendarGoal);

        // when
        String newGoalName = calendarService.changeGoalName(YEAR_MONTH, GOAL_NAME, GOAL_NAME2);

        // then
        List<YearMonth> yearMonths = List.of(YEAR_MONTH);
        assertThatThrownBy(() -> calendarService.saveGoal(newGoalName, yearMonths))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더는_일주일간_공부일을_변경할_수_있다() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        calendar.changeWeeklyStudyDate(4);
        calendarRepository.save(calendar);

        // when
        calendarService.changeWeeklyStudyDate(YEAR_MONTH, 7);

        // then
        Calendar savedCalendar = calendarRepository.findByYearAndMonth(YEAR_MONTH).orElseThrow();
        assertThat(savedCalendar.changeWeeklyStudyDate(7)).isFalse();
    }

}

package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.calendar.CalendarService;
import hwicode.schedule.calendar.application.calendar.dto.*;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.goal.GoalForbiddenException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.goal.GoalRepository;
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
    void 캘린더를_추가할_수_있다() {
        // given
        Long userId = 1L;
        CalendarSaveCommand command = new CalendarSaveCommand(userId, YEAR_MONTH);

        // when
        Long calendarId = calendarService.saveCalendar(command);

        // then
        assertThat(calendarRepository.existsById(calendarId)).isTrue();
    }

    @Test
    void 기간을_정하고_목표를_추가하면_기간만큼_목표가_캘린더에_추가된다() {
        // given
        Long userId = 1L;
        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2)
        );

        GoalSaveCommand command = new GoalSaveCommand(userId, GOAL_NAME, yearMonths);

        // when
        Long goalId = calendarService.saveGoal(command);

        // then
        assertThat(goalRepository.existsById(goalId)).isTrue();
        assertThat(calendarGoalRepository.findAll()).hasSize(3);
    }

    @Test
    void 목표를_일정_기간동안_연장할_수_있다() {
        // given
        Long userId = 1L;
        GoalSaveCommand saveCommand = new GoalSaveCommand(userId, GOAL_NAME, List.of(YEAR_MONTH));
        Long goalId = calendarService.saveGoal(saveCommand);

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2),
                YEAR_MONTH.plusMonths(3)
        );

        GoalAddToCalendersCommand command = new GoalAddToCalendersCommand(userId, goalId, yearMonths);

        // when
        calendarService.addGoalToCalendars(command);

        // then
        assertThat(goalRepository.findAll()).hasSize(1);
        assertThat(goalRepository.existsById(goalId)).isTrue();
        assertThat(calendarGoalRepository.findAll()).hasSize(4);
    }

    @Test
    void 목표를_일정_기간동안_연장할_때_소유자가_아니라면_에러가_발생한다() {
        // given
        Long userId = 1L;
        GoalSaveCommand saveCommand = new GoalSaveCommand(userId, GOAL_NAME, List.of(YEAR_MONTH));
        Long goalId = calendarService.saveGoal(saveCommand);

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2),
                YEAR_MONTH.plusMonths(3)
        );

        GoalAddToCalendersCommand command = new GoalAddToCalendersCommand(2L, goalId, yearMonths);

        // when
        assertThatThrownBy(() -> calendarService.addGoalToCalendars(command))
                .isInstanceOf(GoalForbiddenException.class);
    }

    @Test
    void 캘린더는_목표의_이름을_변경할_수_있다() {
        // given
        Long userId = 1L;
        Calendar calendar = new Calendar(YEAR_MONTH, userId);
        Goal goal = new Goal(GOAL_NAME, userId);

        calendarRepository.save(calendar);
        goalRepository.save(goal);

        GoalAddToCalendersCommand addToCalendersCommand = new GoalAddToCalendersCommand(userId, goal.getId(), List.of(YEAR_MONTH));
        calendarService.addGoalToCalendars(addToCalendersCommand);

        GoalModifyNameCommand command = new GoalModifyNameCommand(userId, YEAR_MONTH, GOAL_NAME, GOAL_NAME2);

        // when
        String newGoalName = calendarService.changeGoalName(command);

        // then
        GoalSaveCommand saveCommand = new GoalSaveCommand(userId, newGoalName, List.of(YEAR_MONTH));
        assertThatThrownBy(() -> calendarService.saveGoal(saveCommand))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 캘린더는_일주일간_공부일을_변경할_수_있다() {
        // given
        Long userId = 1L;
        Calendar calendar = new Calendar(YEAR_MONTH, userId);
        calendar.changeWeeklyStudyDate(4);
        calendarRepository.save(calendar);

        CalendarModifyStudyDateCommand command = new CalendarModifyStudyDateCommand(userId, YEAR_MONTH, 7);

        // when
        calendarService.changeWeeklyStudyDate(command);

        // then
        Calendar savedCalendar = calendarRepository.findByYearAndMonth(userId, YEAR_MONTH).orElseThrow();
        assertThat(savedCalendar.changeWeeklyStudyDate(7)).isFalse();
    }

}

package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.GOAL_NAME;
import static hwicode.schedule.calendar.CalendarDataHelper.YEAR_MONTH;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalendarAggregateServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    CalendarAggregateService calendarAggregateService;

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
        Long goalId = calendarAggregateService.saveGoal(GOAL_NAME, yearMonths);

        // then
        assertThat(goalRepository.existsById(goalId)).isTrue();
        assertThat(calendarGoalRepository.findAll()).hasSize(3);
    }

}

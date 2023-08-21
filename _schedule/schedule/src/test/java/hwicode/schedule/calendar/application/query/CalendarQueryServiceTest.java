package hwicode.schedule.calendar.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.CalendarService;
import hwicode.schedule.calendar.application.GoalAggregateService;
import hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse;
import hwicode.schedule.calendar.application.query.dto.GoalQueryResponse;
import hwicode.schedule.calendar.application.query.dto.SubGoalQueryResponse;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoalStatus;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalendarQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    CalendarQueryService calendarQueryService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    GoalAggregateService goalAggregateService;

    @Autowired
    CalendarRepository calendarRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 캘린더를_조회할_수_있다() {
        // given
        YearMonth now = YearMonth.now();
        Calendar calendar = new Calendar(now);
        calendarRepository.save(calendar);

        Long goalId = calendarService.saveGoal(GOAL_NAME, List.of(now));
        Long subGoalId = goalAggregateService.saveSubGoal(goalId, SUB_GOAL_NAME);
        Long subGoalId2 = goalAggregateService.saveSubGoal(goalId, SUB_GOAL_NAME2);

        // when
        CalendarQueryResponse result = calendarQueryService.getCalendarQueryResponse(now);

        // then
        CalendarQueryResponse expectedResponse = createExpectedResponse(now, calendar.getId(), goalId, subGoalId, subGoalId2);
        assertThat(result).isEqualTo(expectedResponse);
    }

    private CalendarQueryResponse createExpectedResponse(YearMonth now, Long calendarId, Long goalId, Long subGoalId, Long subGoalId2) {
        SubGoalQueryResponse subGoalQueryResponse = SubGoalQueryResponse.builder()
                .id(subGoalId)
                .name(SUB_GOAL_NAME)
                .subGoalStatus(SubGoalStatus.TODO)
                .goalId(goalId)
                .build();

        SubGoalQueryResponse subGoalQueryResponse2 = SubGoalQueryResponse.builder()
                .id(subGoalId2)
                .name(SUB_GOAL_NAME2)
                .subGoalStatus(SubGoalStatus.TODO)
                .goalId(goalId)
                .build();

        GoalQueryResponse goalQueryResponse = GoalQueryResponse.builder()
                .id(goalId)
                .name(GOAL_NAME)
                .goalStatus(GoalStatus.TODO)
                .subGoalResponses(List.of(subGoalQueryResponse, subGoalQueryResponse2))
                .build();

        return CalendarQueryResponse.builder()
                .id(calendarId)
                .yearAndMonth(now)
                .weeklyStudyDate(5)
                .goalResponses(List.of(goalQueryResponse))
                .build();
    }

}

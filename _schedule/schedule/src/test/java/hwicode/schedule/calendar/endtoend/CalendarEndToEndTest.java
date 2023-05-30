package hwicode.schedule.calendar.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.CalendarAggregateService;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify.GoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify.WeeklyStudyDateModifyRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CalendarEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    CalendarAggregateService calendarAggregateService;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarGoalRepository calendarGoalRepository;

    @Autowired
    GoalRepository goalRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 목표_생성_요청() {
        //given
        Set<YearMonth> yearMonths = Set.of(
                YEAR_MONTH, YEAR_MONTH.plusMonths(1), YEAR_MONTH.plusMonths(2)
        );

        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, yearMonths);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(goalSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/calendars/goals", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(goalRepository.findAll()).hasSize(1);
        assertThat(calendarGoalRepository.findAll()).hasSize(3);
        assertThat(calendarRepository.findAll()).hasSize(3);
    }

    @Test
    void 캘린더에_목표_추가_요청() {
        //given
        Goal goal = new Goal(GOAL_NAME);
        goalRepository.save(goal);

        Set<YearMonth> yearMonths = Set.of(
                YEAR_MONTH, YEAR_MONTH.plusMonths(1), YEAR_MONTH.plusMonths(2)
        );
        GoalAddToCalendarsRequest goalAddToCalendarsRequest = new GoalAddToCalendarsRequest(yearMonths);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId())
                .contentType(ContentType.JSON)
                .body(goalAddToCalendarsRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/calendars/goals/{goalId}", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        assertThat(goalRepository.findAll()).hasSize(1);
        assertThat(calendarGoalRepository.findAll()).hasSize(3);
        assertThat(calendarRepository.findAll()).hasSize(3);
    }

    @Test
    void 목표_이름_변경_요청() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        CalendarGoal calendarGoal = calendar.addGoal(goal);

        goalRepository.save(goal);
        calendarRepository.save(calendar);
        calendarGoalRepository.save(calendarGoal);

        GoalNameModifyRequest goalNameModifyRequest = new GoalNameModifyRequest(YEAR_MONTH, GOAL_NAME, NEW_GOAL_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("calendarId", calendar.getId())
                .pathParam("goalId", goal.getId())
                .contentType(ContentType.JSON)
                .body(goalNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/calendars/{calendarId}/goals/{goalId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        List<YearMonth> yearMonths = List.of(YEAR_MONTH);
        assertThatThrownBy(() -> calendarAggregateService.saveGoal(NEW_GOAL_NAME, yearMonths))
                .isInstanceOf(CalendarGoalDuplicateException.class);
    }

    @Test
    void 일주일간_공부일_수정_요청() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        calendarRepository.save(calendar);

        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, 6);

        RequestSpecification requestSpecification = given()
                .pathParam("calendarId", calendar.getId())
                .contentType(ContentType.JSON)
                .body(weeklyStudyDateModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/calendars/{calendarId}/weeklyStudyDate", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Calendar savedCalendar = calendarRepository.findByYearAndMonthWithCalendarGoals(YEAR_MONTH).orElseThrow();
        assertThat(savedCalendar.changeWeeklyStudyDate(6)).isFalse();
    }

}

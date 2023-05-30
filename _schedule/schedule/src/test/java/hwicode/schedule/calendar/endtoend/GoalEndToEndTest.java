package hwicode.schedule.calendar.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.GoalAggregateService;
import hwicode.schedule.calendar.domain.*;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotAllDoneException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import hwicode.schedule.calendar.presentation.goal.dto.calendargoal_delete.CalendarGoalDeleteRequest;
import hwicode.schedule.calendar.presentation.goal.dto.goal_status_modify.GoalStatusModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_delete.SubGoalDeleteRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_name_modify.SubGoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_save.SubGoalSaveRequest;
import hwicode.schedule.calendar.presentation.goal.dto.subgoal_status_modify.SubGoalStatusModifyRequest;
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

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GoalEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    GoalAggregateService goalAggregateService;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    SubGoalRepository subGoalRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    CalendarGoalRepository calendarGoalRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 서브_목표_생성_요청() {
        //given
        Goal goal = new Goal(GOAL_NAME);
        goalRepository.save(goal);
        SubGoalSaveRequest subGoalSaveRequest = new SubGoalSaveRequest(SUB_GOAL_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId())
                .contentType(ContentType.JSON)
                .body(subGoalSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/goals/{goalId}/sub-goals", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(subGoalRepository.findAll()).hasSize(1);
    }

    @Test
    void 서브_목표_이름_변경_요청() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        SubGoal subGoal = goal.createSubGoal(SUB_GOAL_NAME);
        goalRepository.save(goal);

        SubGoalNameModifyRequest subGoalNameModifyRequest = new SubGoalNameModifyRequest(SUB_GOAL_NAME, NEW_SUB_GOAL_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId())
                .pathParam("subGoalId", subGoal.getId())
                .contentType(ContentType.JSON)
                .body(subGoalNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/goals/{goalId}/sub-goals/{subGoalId}/name", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        assertThatThrownBy(() -> goalAggregateService.saveSubGoal(goal.getId(), NEW_SUB_GOAL_NAME))
                .isInstanceOf(SubGoalDuplicateException.class);
    }

    @Test
    void 서브_목표_삭제_요청() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        SubGoal subGoal = goal.createSubGoal(SUB_GOAL_NAME);
        goalRepository.save(goal);

        SubGoalDeleteRequest subGoalDeleteRequest = new SubGoalDeleteRequest(SUB_GOAL_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId())
                .pathParam("subGoalId", subGoal.getId())
                .contentType(ContentType.JSON)
                .body(subGoalDeleteRequest);

        // when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/goals/{goalId}/sub-goals/{subGoalId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        boolean isExist = subGoalRepository.existsById(subGoal.getId());
        assertThat(isExist).isFalse();
    }

    @Test
    void 서브_목표_진행_상태_변경_요청() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        SubGoal subGoal = goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goalRepository.save(goal);

        SubGoalStatusModifyRequest subGoalStatusModifyRequest = new SubGoalStatusModifyRequest(SUB_GOAL_NAME, SubGoalStatus.TODO);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId())
                .pathParam("subGoalId", subGoal.getId())
                .contentType(ContentType.JSON)
                .body(subGoalStatusModifyRequest);
        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/goals/{goalId}/sub-goals/{subGoalId}/status", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Goal savedGoal = goalRepository.findGoalWithSubGoals(goal.getId()).orElseThrow();
        assertThatThrownBy(() -> savedGoal.changeGoalStatus(GoalStatus.DONE))
                .isInstanceOf(SubGoalNotAllDoneException.class);
    }

    @Test
    void 목표_진행_상태_변경_요청() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        goalRepository.save(goal);

        GoalStatusModifyRequest goalStatusModifyRequest = new GoalStatusModifyRequest(GoalStatus.PROGRESS);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId())
                .contentType(ContentType.JSON)
                .body(goalStatusModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/goals/{goalId}/status", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Goal savedGoal = goalRepository.findById(goal.getId()).orElseThrow();
        assertThat(savedGoal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표_삭제_요청() {
        // given
        Goal goal = new Goal(GOAL_NAME);
        goalRepository.save(goal);

        RequestSpecification requestSpecification = given()
                .pathParam("goalId", goal.getId());

        // when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/goals/{goalId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        boolean isExist = goalRepository.existsById(goal.getId());
        assertThat(isExist).isFalse();
    }

    @Test
    void 캘린더에_있는_목표_삭제_요청() {
        // given
        Calendar calendar = new Calendar(YEAR_MONTH);
        Goal goal = new Goal(GOAL_NAME);
        CalendarGoal calendarGoal = calendar.addGoal(goal);

        goalRepository.save(goal);
        calendarRepository.save(calendar);
        calendarGoalRepository.save(calendarGoal);

        CalendarGoalDeleteRequest calendarGoalDeleteRequest = new CalendarGoalDeleteRequest(YEAR_MONTH);

        RequestSpecification requestSpecification = given()
                .pathParam("calendarId", calendar.getId())
                .pathParam("goalId", goal.getId())
                .contentType(ContentType.JSON)
                .body(calendarGoalDeleteRequest);

        // when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/calendars/{calendarId}/goals/{goalId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(calendarGoalRepository.findAll()).isEmpty();
    }

}

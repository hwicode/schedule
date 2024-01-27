package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.TaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TaskCheckerEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskCheckerRepository taskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist(1L);
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.FIRST);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(taskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", dailyChecklist.getId());

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<TaskChecker> all = taskCheckerRepository.findAll();
        Assertions.assertThat(all).hasSize(1);
    }

    @Test
    void 과제_삭제_요청() {
        //given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskCheckerSubService.saveTaskChecker(
                new TaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("taskName", TASK_CHECKER_NAME);

        //when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}", dailyChecklist.getId(), taskId);

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Assertions.assertThat(taskCheckerRepository.existsById(taskId)).isFalse();
    }

    @Test
    void 과제체커_진행_상태_변경_요청() {
        //given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(
                new TaskSaveCommand(userId, dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, TaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(taskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status", dailyChecklist.getId(), taskCheckerId);

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskChecker taskChecker = taskCheckerRepository.findById(taskCheckerId).orElseThrow();
        assertThat(taskChecker.isDone()).isTrue();
    }

    @Test
    void 과제체커_어려움_점수_변경_요청() {
        //given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(
                new TaskSaveCommand(userId, dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.HARD);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(taskDifficultyModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/difficulty", dailyChecklist.getId(), taskCheckerId);

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskChecker taskChecker = taskCheckerRepository.findById(taskCheckerId).orElseThrow();
        assertThat(taskChecker.getDifficultyScore()).isEqualTo(Difficulty.HARD.getValue());
    }

    @Test
    void 과제체커_이름_변경_요청() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(
                new TaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        TaskCheckerNameModifyRequest taskCheckerNameModifyRequest = new TaskCheckerNameModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(taskCheckerNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/name", dailyChecklist.getId(), taskCheckerId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskSaveCommand command = new TaskSaveCommand(userId, dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);
        assertThatThrownBy(() -> taskCheckerSubService.saveTaskChecker(command))
                .isInstanceOf(TaskCheckerNameDuplicationException.class);
    }

}

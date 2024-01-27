package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.TaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.*;
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

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubTaskCheckerEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

    @Autowired
    SubTaskCheckerSubService subTaskCheckerSubService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskCheckerRepository subTaskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 서브_과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist(1L);
        TaskChecker taskChecker = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(subTaskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks", dailyChecklist.getId(), taskChecker.getId());

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(subTaskCheckerRepository.findAll()).hasSize(1);
    }

    @Test
    void 서브_과제_삭제_요청() {
        //given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        TaskChecker taskChecker = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskCheckerId = subTaskCheckerSubService.saveSubTaskChecker(
                new SubTaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME)
        );

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("taskName", TASK_CHECKER_NAME)
                .queryParam("subTaskName", SUB_TASK_CHECKER_NAME);

        //when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}",
                        dailyChecklist.getId(), taskChecker.getId(), subTaskCheckerId);

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskCheckerRepository.existsById(subTaskCheckerId)).isFalse();
    }

    @Test
    void 서브_과제체커_진행_상태_변경_요청() {
        //given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        TaskChecker taskChecker = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskCheckerId = subTaskCheckerSubService.saveSubTaskChecker(
                new SubTaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
        );

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        dailyChecklist.getId(), taskChecker.getId(), subTaskCheckerId);

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskChecker subTaskChecker = subTaskCheckerRepository.findById(subTaskCheckerId).orElseThrow();
        assertThat(subTaskChecker.isSameStatus(SubTaskStatus.DONE)).isTrue();
    }

    @Test
    void 서브_과제체커_이름_변경_요청() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(
                new TaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        Long subTaskCheckerId = subTaskCheckerSubService.saveSubTaskChecker(
                new SubTaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME)
        );

        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = new SubTaskCheckerNameModifyRequest(taskCheckerId, SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(subTaskCheckerNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name", taskCheckerId, subTaskCheckerId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskSaveCommand command = new SubTaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> subTaskCheckerSubService.saveSubTaskChecker(command))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}

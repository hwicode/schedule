package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.application.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubTaskCheckerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerService taskCheckerService;

    @Autowired
    SubTaskCheckerService subTaskCheckerService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskCheckerRepository subTaskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 서브_과제체커_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        TaskChecker taskChecker = new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, Difficulty.NORMAL);
        dailyChecklist.addTaskChecker(taskChecker);
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskCheckerId = subTaskCheckerService.saveSubTaskChecker(
                new SubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
        );

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("dailyToDoListId", dailyChecklist.getId())
                .pathParam("taskId", taskChecker.getId())
                .pathParam("subTaskId", subTaskCheckerId)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskChecker subTaskChecker = subTaskCheckerRepository.findById(subTaskCheckerId).orElseThrow();
        assertThat(subTaskChecker.isSameStatus(SubTaskStatus.DONE)).isTrue();
    }

    @Test
    void 서브_과제체커_이름_변경_요청() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerService.saveTaskChecker(
                new TaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL)
        );

        Long subTaskCheckerId = subTaskCheckerService.saveSubTaskChecker(
                new SubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME)
        );

        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = new SubTaskCheckerNameModifyRequest(taskCheckerId, SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("taskId", taskCheckerId)
                .pathParam("subTaskId", subTaskCheckerId)
                .contentType(ContentType.JSON)
                .body(subTaskCheckerNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = new SubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> subTaskCheckerService.saveSubTaskChecker(subTaskCheckerSaveRequest))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}

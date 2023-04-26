package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
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

    private DailyChecklist createDailyChecklistWithTaskChecker() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTaskChecker(new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    void 서브_과제체커_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskCheckerId = subTaskCheckerService.saveSubTaskChecker(
                createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
        );

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SubTaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("dailyToDoListId", DAILY_CHECKLIST_ID)
                .pathParam("taskName", NEW_TASK_CHECKER_NAME)
                .pathParam("subTaskName", NEW_SUB_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/subtasks/{subTaskName}/status", port));

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
                createTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL)
        );

        subTaskCheckerService.saveSubTaskChecker(
                createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME)
        );

        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = createSubTaskCheckerNameModifyRequest(taskCheckerId, NEW_SUB_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("dailyToDoListId", DAILY_CHECKLIST_ID)
                .pathParam("taskName", NEW_TASK_CHECKER_NAME)
                .pathParam("subTaskName", SUB_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskCheckerNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskName}/subtasks/{subTaskName}/name", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> subTaskCheckerService.saveSubTaskChecker(subTaskCheckerSaveRequest))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}

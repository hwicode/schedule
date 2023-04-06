package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyRequest;
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

import java.util.List;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubTaskCheckerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskCheckerService subTaskCheckerService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private DailyChecklist createDailyChecklistWithTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task(TASK_NAME, TaskStatus.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    void 서브_과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveRequest subTaskSaveRequest = createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(subTaskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/checklist/subtasks", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<SubTaskChecker> all = subTaskRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void 서브_과제_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskId = subTaskCheckerService.saveSubTask(
                createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME)
        );

        SubTaskDeleteRequest subTaskDeleteRequest = createSubTaskDeleteRequest(dailyChecklist.getId(), TASK_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", NEW_SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/checklist/subtasks/{subTaskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskRepository.existsById(subTaskId)).isFalse();
    }

    @Test
    void 서브_과제_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskId = subTaskCheckerService.saveSubTask(
                createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME)
        );

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_NAME, SubTaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", NEW_SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/subtasks/{subTaskName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskChecker subTaskChecker = subTaskRepository.findById(subTaskId).orElseThrow();
        assertThat(subTaskChecker.isSameStatus(SubTaskStatus.DONE)).isTrue();
    }
}

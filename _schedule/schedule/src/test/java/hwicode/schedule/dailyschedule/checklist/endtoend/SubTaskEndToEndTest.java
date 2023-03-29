package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyRequest;
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
public class SubTaskEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskService subTaskService;

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
        dailyChecklist.addTask(new Task(TASK_NAME, Status.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    public void 서브_과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(subTaskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/subtasks", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<SubTask> all = subTaskRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void 서브_과제_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskId = subTaskService.saveSubTask(new SubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME));

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyChecklist.getId(), TASK_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", NEW_SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/subtasks/{subTaskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskRepository.existsById(subTaskId)).isFalse();
    }

    @Test
    public void 서브_과제_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskId = subTaskService.saveSubTask(new SubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME));

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_NAME, Status.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", NEW_SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/subtasks/{subTaskName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTask subTask = subTaskRepository.findById(subTaskId).orElseThrow();
        assertThat(subTask.isSameStatus(Status.DONE)).isTrue();
    }
}

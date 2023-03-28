package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
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

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.SUB_TASK_NAME;
import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.TASK_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SubTaskEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskService taskService;

    @Autowired
    SubTaskService subTaskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    Long dailyChecklistId;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    public void 서브_과제_생성_요청() {
        //given
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.save(new DailyChecklist());
        dailyChecklistId = savedDailyChecklist.getId();

        taskService.saveTask(new TaskSaveRequest(dailyChecklistId, TASK_NAME));

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyChecklistId, TASK_NAME, SUB_TASK_NAME);

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
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.save(new DailyChecklist());
        dailyChecklistId = savedDailyChecklist.getId();

        taskService.saveTask(new TaskSaveRequest(dailyChecklistId, TASK_NAME));
        Long subTaskId = subTaskService.saveSubTask(new SubTaskSaveRequest(dailyChecklistId, TASK_NAME, SUB_TASK_NAME));

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyChecklistId, TASK_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam(SUB_TASK_NAME, SUB_TASK_NAME)
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
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.save(new DailyChecklist());
        dailyChecklistId = savedDailyChecklist.getId();

        taskService.saveTask(new TaskSaveRequest(dailyChecklistId, TASK_NAME));
        Long subTaskId = subTaskService.saveSubTask(new SubTaskSaveRequest(dailyChecklistId, TASK_NAME, SUB_TASK_NAME));

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(dailyChecklistId, TASK_NAME, Status.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam(SUB_TASK_NAME, SUB_TASK_NAME)
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

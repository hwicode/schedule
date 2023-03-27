package hwicode.schedule.dailyschedule.checklist;

import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SubTaskEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    SubTaskService subTaskService;

    Long dailyChecklistId;
    String taskName = "taskName";
    String subTaskName = "subTaskName";

    @AfterEach
    void clearDatabase() {
        dailyChecklistRepository.deleteAll();
        subTaskRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void 서브_과제_생성_요청() {
        //given
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.save(new DailyChecklist());
        dailyChecklistId = savedDailyChecklist.getId();

        taskService.saveTask(new TaskSaveRequest(dailyChecklistId, taskName));

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyChecklistId, taskName, subTaskName);

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

}

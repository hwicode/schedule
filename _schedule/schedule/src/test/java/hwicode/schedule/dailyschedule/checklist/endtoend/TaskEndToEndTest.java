package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify.TaskStatusModifyRequest;
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

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.TASK_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaskEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskService taskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    public void 과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyChecklist.getId(), TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(taskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/tasks", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<Task> all = taskRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void 과제_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskService.saveTask(new TaskSaveRequest(dailyChecklist.getId(), TASK_NAME));

        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(dailyChecklist.getId());

        RequestSpecification requestSpecification = given()
                .pathParam(TASK_NAME, TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/tasks/{taskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(taskRepository.existsById(taskId)).isFalse();
    }

    @Test
    public void 과제_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskService.saveTask(new TaskSaveRequest(dailyChecklist.getId(), TASK_NAME));

        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(dailyChecklist.getId(), Status.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam(TASK_NAME, TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/tasks/{taskName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Task task = taskRepository.findById(taskId).orElseThrow();
        assertThat(task.isDone()).isTrue();
    }

    @Test
    public void 과제_어려움_점수_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskService.saveTask(new TaskSaveRequest(dailyChecklist.getId(), TASK_NAME));

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        RequestSpecification requestSpecification = given()
                .pathParam(TASK_NAME, TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskDifficultyModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/tasks/{taskName}/difficulty", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Task task = taskRepository.findById(taskId).orElseThrow();
        assertThat(task.getDifficultyScore()).isEqualTo(Difficulty.HARD.getValue());
    }
}

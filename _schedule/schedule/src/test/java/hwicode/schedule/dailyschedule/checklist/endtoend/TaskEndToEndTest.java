package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyRequest;
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
class TaskEndToEndTest {

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
    void 과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(taskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/checklist/tasks", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<Task> all = taskRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void 과제_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskService.saveTask(
                createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME)
        );

        TaskDeleteRequest taskDeleteRequest = createTaskDeleteRequest(dailyChecklist.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("taskName", NEW_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/checklist/tasks/{taskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(taskRepository.existsById(taskId)).isFalse();
    }

    @Test
    void 과제_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskService.saveTask(
                createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME)
        );

        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(dailyChecklist.getId(), TaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("taskName", NEW_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/tasks/{taskName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Task task = taskRepository.findById(taskId).orElseThrow();
        assertThat(task.isDone()).isTrue();
    }

    @Test
    void 과제_어려움_점수_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskService.saveTask(
                createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME)
        );

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        RequestSpecification requestSpecification = given()
                .pathParam("taskName", NEW_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskDifficultyModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/tasks/{taskName}/difficulty", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Task task = taskRepository.findById(taskId).orElseThrow();
        assertThat(task.getDifficultyScore()).isEqualTo(Difficulty.HARD.getValue());
    }
}

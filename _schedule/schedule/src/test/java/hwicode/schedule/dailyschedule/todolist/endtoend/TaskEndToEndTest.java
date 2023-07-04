package hwicode.schedule.dailyschedule.todolist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.*;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
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

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TaskEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제_생성_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyToDoList.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.FIRST);

        RequestSpecification requestSpecification = given()
                .pathParam("dailyToDoListId", dailyToDoList.getId())
                .contentType(ContentType.JSON)
                .body(taskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<Task> all = taskRepository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    void 과제_삭제_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        Long taskId = taskSaveAndDeleteService.save(
                new TaskSaveRequest(dailyToDoList.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(dailyToDoList.getId(), taskId, TASK_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("dailyToDoListId", DAILY_TO_DO_LIST_ID)
                .pathParam("taskId", TASK_ID)
                .contentType(ContentType.JSON)
                .body(taskDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(taskRepository.existsById(taskId)).isFalse();
    }

    @Test
    void 과제_정보_변경_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        Long taskId = taskSaveAndDeleteService.save(
                new TaskSaveRequest(dailyToDoList.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        TaskInformationModifyRequest taskInformationModifyRequest = new TaskInformationModifyRequest(Priority.THIRD, Importance.THIRD);

        RequestSpecification requestSpecification = given()
                .pathParam("taskId", taskId)
                .contentType(ContentType.JSON)
                .body(taskInformationModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/tasks/{taskId}/information", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Task task = taskRepository.findById(taskId).orElseThrow();
        assertThat(task.changePriority(Priority.THIRD)).isFalse();
        assertThat(task.changeImportance(Importance.THIRD)).isFalse();
    }
}

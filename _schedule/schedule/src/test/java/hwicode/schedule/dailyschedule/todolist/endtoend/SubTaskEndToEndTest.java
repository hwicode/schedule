package hwicode.schedule.dailyschedule.todolist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.save.SubTaskSaveRequest;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubTaskEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @Autowired
    SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @Autowired
    SubTaskRepository subTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 서브_과제_생성_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        taskSaveAndDeleteService.save(
                createTaskSaveRequest(dailyToDoList.getId(), TASK_NAME)
        );

        SubTaskSaveRequest subTaskSaveRequest = createSubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, SUB_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(subTaskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/todolist/subtasks", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<SubTask> all = subTaskRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void 과제_삭제_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        taskSaveAndDeleteService.save(
                createTaskSaveRequest(dailyToDoList.getId(), TASK_NAME)
        );

        Long subTaskId = subTaskSaveAndDeleteService.save(
                createSubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, SUB_TASK_NAME)
        );

        SubTaskDeleteRequest subTaskDeleteRequest = createSubTaskDeleteRequest(dailyToDoList.getId(), TASK_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/todolist/subtasks/{subTaskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskRepository.existsById(subTaskId)).isFalse();
    }

}

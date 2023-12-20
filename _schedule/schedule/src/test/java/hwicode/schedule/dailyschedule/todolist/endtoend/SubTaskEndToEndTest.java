package hwicode.schedule.dailyschedule.todolist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
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

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubTaskEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

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

        taskCheckerSubService.saveTaskChecker(
                new TaskSaveRequest(dailyToDoList.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, SUB_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(subTaskSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks", dailyToDoList.getId(), TASK_ID);

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(subTaskRepository.findAll()).hasSize(1);
    }

    @Test
    void 서브_과제_삭제_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        taskCheckerSubService.saveTaskChecker(
                new TaskSaveRequest(dailyToDoList.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
        );

        Long subTaskId = subTaskSaveAndDeleteService.save(
                new SubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, SUB_TASK_NAME)
        );

        RequestSpecification requestSpecification = given()
                .port(port)
                .queryParam("taskName", TASK_NAME)
                .queryParam("subTaskName", SUB_TASK_NAME);

        //when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}",
                        dailyToDoList.getId(), TASK_ID, SUB_TASK_ID);

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskRepository.existsById(subTaskId)).isFalse();
    }

}

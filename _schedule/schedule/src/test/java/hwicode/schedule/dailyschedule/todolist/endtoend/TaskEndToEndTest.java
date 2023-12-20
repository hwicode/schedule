package hwicode.schedule.dailyschedule.todolist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
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

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TaskEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제_정보_변경_요청() {
        //given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        Task task = new Task(dailyToDoList, TASK_NAME);
        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(task);

        TaskInformationModifyRequest taskInformationModifyRequest = new TaskInformationModifyRequest(Priority.THIRD, Importance.THIRD);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(taskInformationModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/tasks/{taskId}/information", task.getId());

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        Task savedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(savedTask.changePriority(Priority.THIRD)).isFalse();
        assertThat(savedTask.changeImportance(Importance.THIRD)).isFalse();
    }
}

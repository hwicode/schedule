package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.delete.TaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
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
class TaskCheckerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerService taskCheckerService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskCheckerRepository taskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        TaskCheckerSaveRequest taskCheckerSaveRequest = createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(taskCheckerSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<TaskChecker> all = taskCheckerRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void 과제_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskCheckerService.saveTask(
                createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME)
        );

        TaskCheckerDeleteRequest taskCheckerDeleteRequest = createTaskDeleteRequest(dailyChecklist.getId());

        RequestSpecification requestSpecification = given()
                .pathParam("taskName", NEW_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskCheckerDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers/{taskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(taskCheckerRepository.existsById(taskId)).isFalse();
    }

    @Test
    void 과제_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskCheckerService.saveTask(
                createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME)
        );

        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(dailyChecklist.getId(), TaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("taskName", NEW_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers/{taskName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskChecker taskChecker = taskCheckerRepository.findById(taskId).orElseThrow();
        assertThat(taskChecker.isDone()).isTrue();
    }

    @Test
    void 과제_어려움_점수_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskId = taskCheckerService.saveTask(
                createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME)
        );

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        RequestSpecification requestSpecification = given()
                .pathParam("taskName", NEW_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(taskDifficultyModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers/{taskName}/difficulty", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskChecker taskChecker = taskCheckerRepository.findById(taskId).orElseThrow();
        assertThat(taskChecker.getDifficultyScore()).isEqualTo(Difficulty.HARD.getValue());
    }
}
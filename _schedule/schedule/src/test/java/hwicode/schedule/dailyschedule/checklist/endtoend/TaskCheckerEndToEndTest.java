package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.application.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
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

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void 과제체커_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerService.saveTaskChecker(
                createTaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL)
        );

        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(dailyChecklist.getId(), TaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("taskCheckerName", NEW_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(taskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers/{taskCheckerName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskChecker taskChecker = taskCheckerRepository.findById(taskCheckerId).orElseThrow();
        assertThat(taskChecker.isDone()).isTrue();
    }

    @Test
    void 과제체커_어려움_점수_변경_요청() {
        //given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerService.saveTaskChecker(
                createTaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL)
        );

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        RequestSpecification requestSpecification = given()
                .pathParam("taskCheckerName", NEW_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(taskDifficultyModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers/{taskCheckerName}/difficulty", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskChecker taskChecker = taskCheckerRepository.findById(taskCheckerId).orElseThrow();
        assertThat(taskChecker.getDifficultyScore()).isEqualTo(Difficulty.HARD.getValue());
    }

    @Test
    void 과제체커_이름_변경_요청() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        taskCheckerService.saveTaskChecker(
                createTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL)
        );

        TaskCheckerNameModifyRequest taskCheckerNameModifyRequest = createTaskCheckerNameModifyRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("taskCheckerName", TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(taskCheckerNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/taskCheckers/{taskCheckerName}/name", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TaskCheckerSaveRequest taskCheckerSaveRequest = createTaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);
        assertThatThrownBy(() -> taskCheckerService.saveTaskChecker(taskCheckerSaveRequest))
                .isInstanceOf(TaskCheckerNameDuplicationException.class);
    }

}

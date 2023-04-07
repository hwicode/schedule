package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
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
class SubTaskCheckerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskCheckerService subTaskCheckerService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskCheckerRepository subTaskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private DailyChecklist createDailyChecklistWithTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new TaskChecker(TASK_NAME, TaskStatus.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    void 서브_과제_생성_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME);

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(subTaskCheckerSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/checklist/subtaskCheckers", port));

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        List<SubTaskChecker> all = subTaskCheckerRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void 서브_과제_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskId = subTaskCheckerService.saveSubTask(
                createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME)
        );

        SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest = createSubTaskDeleteRequest(dailyChecklist.getId(), TASK_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", NEW_SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskCheckerDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/checklist/subtaskCheckers/{subTaskName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskCheckerRepository.existsById(subTaskId)).isFalse();
    }

    @Test
    void 서브_과제_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTask();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskId = subTaskCheckerService.saveSubTask(
                createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME, NEW_SUB_TASK_NAME)
        );

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_NAME, SubTaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskName", NEW_SUB_TASK_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/subtaskCheckers/{subTaskName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskChecker subTaskChecker = subTaskCheckerRepository.findById(subTaskId).orElseThrow();
        assertThat(subTaskChecker.isSameStatus(SubTaskStatus.DONE)).isTrue();
    }
}

package hwicode.schedule.dailyschedule.checklist.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyRequest;
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

import java.util.List;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubTaskCheckerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerService taskCheckerService;

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

    private DailyChecklist createDailyChecklistWithTaskChecker() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTaskChecker(new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    void 서브_과제체커_생성_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

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
    void 서브_과제체커_삭제_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskCheckerId = subTaskCheckerService.saveSubTaskChecker(
                createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
        );

        SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest = createSubTaskCheckerDeleteRequest(dailyChecklist.getId(), TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskCheckerName", NEW_SUB_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskCheckerDeleteRequest);

        //when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/checklist/subtaskCheckers/{subTaskCheckerName}", port));

        //then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(subTaskCheckerRepository.existsById(subTaskCheckerId)).isFalse();
    }

    @Test
    void 서브_과제체커_진행_상태_변경_요청() {
        //given
        DailyChecklist dailyChecklist = createDailyChecklistWithTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        Long subTaskCheckerId = subTaskCheckerService.saveSubTaskChecker(
                createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
        );

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SubTaskStatus.DONE);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskCheckerName", NEW_SUB_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskStatusModifyRequest);

        //when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/subtaskCheckers/{subTaskCheckerName}/status", port));

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskChecker subTaskChecker = subTaskCheckerRepository.findById(subTaskCheckerId).orElseThrow();
        assertThat(subTaskChecker.isSameStatus(SubTaskStatus.DONE)).isTrue();
    }

    @Test
    void 서브_과제체커_이름_변경_요청() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklistRepository.save(dailyChecklist);

        Long taskCheckerId = taskCheckerService.saveTaskChecker(
                createTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, Difficulty.NORMAL)
        );

        subTaskCheckerService.saveSubTaskChecker(
                createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME)
        );

        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = createSubTaskCheckerNameModifyRequest(taskCheckerId, NEW_SUB_TASK_CHECKER_NAME);

        RequestSpecification requestSpecification = given()
                .pathParam("subTaskCheckerName", SUB_TASK_CHECKER_NAME)
                .contentType(ContentType.JSON)
                .body(subTaskCheckerNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/checklist/subtaskCheckers/{subTaskCheckerName}/name", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = createSubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> subTaskCheckerService.saveSubTaskChecker(subTaskCheckerSaveRequest))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}

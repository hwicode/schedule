package hwicode.schedule.dailyschedule.review.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.application.ReviewTaskService;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewTaskRepository;
import hwicode.schedule.dailyschedule.review.presentation.reviewtask.dto.TaskReviewRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewTaskEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewTaskService reviewTaskService;

    @Autowired
    ReviewTaskRepository reviewTaskRepository;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @Autowired
    ReviewDateTaskRepository reviewDateTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제_복습_요청() {
        // given
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewRequest taskReviewRequest = new TaskReviewRequest(reviewCycle.getId(), START_DATE);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(taskReviewRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/tasks/{taskId}/review", reviewTask.getId());

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());
        assertThat(reviewDateTaskRepository.findAll()).hasSize(cycle.size());
    }

    @Test
    void 과제_복습_취소_요청() {
        // given
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        reviewTaskService.reviewTask(reviewTask.getId(), reviewCycle.getId(), START_DATE);

        RequestSpecification requestSpecification = given()
                .port(port);

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/tasks/{taskId}/review", reviewTask.getId());

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(reviewDateTaskRepository.findAll()).isEmpty();
    }

}

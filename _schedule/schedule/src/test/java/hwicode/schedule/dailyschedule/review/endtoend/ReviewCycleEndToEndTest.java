package hwicode.schedule.dailyschedule.review.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.review.ReviewDataHelper;
import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleSaveCommand;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.NEW_REVIEW_CYCLE_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewCycleEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewCycleAggregateService reviewCycleAggregateService;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @Autowired
    TokenProvider tokenProvider;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 복습_주기_생성_요청() {
        // given
        Long userId = 1L;
        String accessToken = ReviewDataHelper.createAccessToken(tokenProvider, userId);

        Set<Integer> cycle = Set.of(1, 2, 3, 4, 5);

        ReviewCycleSaveRequest reviewCycleSaveRequest = new ReviewCycleSaveRequest(REVIEW_CYCLE_NAME, cycle);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(reviewCycleSaveRequest);

        // when
        Response response = requestSpecification.when()
                .post("/dailyschedule/review-cycles");

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(reviewCycleRepository.findAll()).hasSize(1);
    }

    @Test
    void 복습_주기_이름_변경_요청() {
        // given
        Long userId = 1L;
        String accessToken = ReviewDataHelper.createAccessToken(tokenProvider, userId);

        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycleSaveCommand saveCommand = new ReviewCycleSaveCommand(userId, REVIEW_CYCLE_NAME, cycle);
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(saveCommand);

        ReviewCycleNameModifyRequest reviewCycleNameModifyRequest = new ReviewCycleNameModifyRequest(NEW_REVIEW_CYCLE_NAME);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(reviewCycleNameModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/review-cycles/{reviewCycleId}/name", reviewCycleId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
        ReviewCycle savedReviewCycle = reviewCycleRepository.findById(reviewCycleId).orElseThrow();
        assertThat(savedReviewCycle.changeName(NEW_REVIEW_CYCLE_NAME)).isFalse();
    }

    @Test
    void 복습_주기_주기_변경_요청() {
        // given
        Long userId = 1L;
        String accessToken = ReviewDataHelper.createAccessToken(tokenProvider, userId);

        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycleSaveCommand saveCommand = new ReviewCycleSaveCommand(userId, REVIEW_CYCLE_NAME, cycle);
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(saveCommand);

        Set<Integer> newCycle = Set.of(2, 4, 5, 8, 13, 20);
        ReviewCycleCycleModifyRequest reviewCycleCycleModifyRequest = new ReviewCycleCycleModifyRequest(newCycle);

        RequestSpecification requestSpecification = given().port(port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(ContentType.JSON)
                .body(reviewCycleCycleModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/review-cycles/{reviewCycleId}/cycle", reviewCycleId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        ReviewCycle savedReviewCycle = reviewCycleRepository.findById(reviewCycleId).orElseThrow();
        assertThat(savedReviewCycle.getCycle())
                .hasSize(newCycle.size())
                .isEqualTo(reviewCycleCycleModifyRequest.getCycle());
    }

    @Test
    void 복습_주기_삭제_요청() {
        // given
        Long userId = 1L;
        String accessToken = ReviewDataHelper.createAccessToken(tokenProvider, userId);

        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycleSaveCommand saveCommand = new ReviewCycleSaveCommand(userId, REVIEW_CYCLE_NAME, cycle);
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(saveCommand);

        RequestSpecification requestSpecification = given().port(this.port)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/review-cycles/{reviewCycleId}", reviewCycleId);

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(reviewCycleRepository.existsById(reviewCycleId)).isFalse();
    }

}

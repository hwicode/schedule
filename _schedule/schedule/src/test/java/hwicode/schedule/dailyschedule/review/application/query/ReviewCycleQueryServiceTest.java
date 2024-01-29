package hwicode.schedule.dailyschedule.review.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.application.query.dto.ReviewCycleQueryResponse;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewCycleQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewCycleQueryService reviewCycleQueryService;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 모든_복습_주기의_조회를_요청할_수_있다() {
        // given
        Long userId = 1L;
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60);
        List<Integer> reviewCycleDates2 = List.of(4, 5, 6, 10, 20, 50);
        List<Integer> reviewCycleDates3 = List.of(2, 5, 7, 8, 9, 12, 30);

        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates, userId);
        ReviewCycle reviewCycle2 = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates2, userId);
        ReviewCycle reviewCycle3 = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates3, userId);

        reviewCycleRepository.saveAll(List.of(reviewCycle, reviewCycle2, reviewCycle3));

        // when
        List<ReviewCycleQueryResponse> reviewCycleQueryResponses = reviewCycleQueryService.getReviewCycleQueryResponses();

        // then
        assertThat(reviewCycleQueryResponses.get(0).getReviewCycleDates()).isEqualTo(reviewCycleDates);
        assertThat(reviewCycleQueryResponses.get(1).getReviewCycleDates()).isEqualTo(reviewCycleDates2);
        assertThat(reviewCycleQueryResponses.get(2).getReviewCycleDates()).isEqualTo(reviewCycleDates3);
    }

}

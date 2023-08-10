package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.NEW_REVIEW_CYCLE_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewCycleAggregateServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewCycleAggregateService reviewCycleAggregateService;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 복습_주기를_생성할_수_있다() {
        // given
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);

        // when
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(REVIEW_CYCLE_NAME, cycle);

        // then
        assertThat(reviewCycleRepository.existsById(reviewCycleId)).isTrue();
    }

    @Test
    void 복습_주기의_이름을_변경할_수_있다() {
        // given
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(REVIEW_CYCLE_NAME, cycle);

        // when
        String newName = reviewCycleAggregateService.changeReviewCycleName(reviewCycleId, NEW_REVIEW_CYCLE_NAME);

        // then
        ReviewCycle savedReviewCycle = reviewCycleRepository.findById(reviewCycleId).orElseThrow();
        assertThat(savedReviewCycle.changeName(newName)).isFalse();
    }

}

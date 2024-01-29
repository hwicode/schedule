package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleDeleteCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleModifyCycleCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleModifyNameCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleSaveCommand;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.exception.domain.review_cycle.ReviewCycleForbiddenException;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.NEW_REVIEW_CYCLE_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycleSaveCommand command = new ReviewCycleSaveCommand(userId, REVIEW_CYCLE_NAME, cycle);
        // when
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(command);

        // then
        assertThat(reviewCycleRepository.existsById(reviewCycleId)).isTrue();
    }

    @Test
    void 복습_주기의_이름을_변경할_수_있다() {
        // given
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        ReviewCycleModifyNameCommand command = new ReviewCycleModifyNameCommand(userId, reviewCycle.getId(), NEW_REVIEW_CYCLE_NAME);
        // when
        String newName = reviewCycleAggregateService.changeReviewCycleName(command);

        // then
        ReviewCycle savedReviewCycle = reviewCycleRepository.findById(reviewCycle.getId()).orElseThrow();
        assertThat(savedReviewCycle.changeName(newName)).isFalse();
    }

    @Test
    void 복습_주기의_이름을_변경할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        ReviewCycleModifyNameCommand command = new ReviewCycleModifyNameCommand(2L, reviewCycle.getId(), NEW_REVIEW_CYCLE_NAME);
        // when
        assertThatThrownBy(() -> reviewCycleAggregateService.changeReviewCycleName(command))
                .isInstanceOf(ReviewCycleForbiddenException.class);
    }

    @Test
    void 복습_주기의_주기을_변경할_수_있다() {
        // given
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        List<Integer> newCycle = List.of(2, 4, 5, 8, 13, 20);
        ReviewCycleModifyCycleCommand command = new ReviewCycleModifyCycleCommand(userId, reviewCycle.getId(), newCycle);

        // when
        reviewCycleAggregateService.changeCycle(command);

        // then
        ReviewCycle savedReviewCycle = reviewCycleRepository.findById(reviewCycle.getId()).orElseThrow();
        assertThat(savedReviewCycle.getCycle())
                .hasSize(newCycle.size())
                .isEqualTo(newCycle);
    }

    @Test
    void 복습_주기의_주기을_변경할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        List<Integer> newCycle = List.of(2, 4, 5, 8, 13, 20);
        ReviewCycleModifyCycleCommand command = new ReviewCycleModifyCycleCommand(2L, reviewCycle.getId(), newCycle);

        // when then
        assertThatThrownBy(() -> reviewCycleAggregateService.changeCycle(command))
                .isInstanceOf(ReviewCycleForbiddenException.class);
    }

    @Test
    void 복습_주기를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        ReviewCycleDeleteCommand command = new ReviewCycleDeleteCommand(userId, reviewCycle.getId());

        // when
        reviewCycleAggregateService.deleteReviewCycle(command);

        // then
        assertThat(reviewCycleRepository.existsById(reviewCycle.getId())).isFalse();
    }

    @Test
    void 복습_주기를_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        List<Integer> cycle = List.of(1, 2, 3, 4, 5);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        ReviewCycleDeleteCommand command = new ReviewCycleDeleteCommand(2L, reviewCycle.getId());

        // when then
        assertThatThrownBy(() -> reviewCycleAggregateService.deleteReviewCycle(command))
                .isInstanceOf(ReviewCycleForbiddenException.class);
    }

}

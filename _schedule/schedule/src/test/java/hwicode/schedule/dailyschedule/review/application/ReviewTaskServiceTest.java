package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewCycleNotFoundException;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewTaskNotFoundException;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class ReviewTaskServiceTest {

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
    void 복습_주기에_맞춰_과제를_복습할_수_있다() {
        // given
        ReviewTask reviewTask = new ReviewTask(REVIEW_TASK_NAME, null, null, null);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        // when
        reviewTaskService.reviewTask(reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // then
        assertThat(reviewDateTaskRepository.findAll()).hasSize(cycle.size());
    }

    private static Stream<List<Integer>> createCycle() {
        return Stream.of(
                List.of(1, 2, 5, 7),
                List.of(4, 7, 20, 23, 25, 30, 50),
                List.of(1, 2, 4, 8, 16, 32),
                List.of(7, 14, 21, 28, 35, 42, 49, 56),
                List.of(7)
        );
    }

    @ParameterizedTest
    @MethodSource("createCycle")
    void 과제의_복습을_취소할_수_있다(List<Integer> cycle) {
        // given
        ReviewTask reviewTask = new ReviewTask(REVIEW_TASK_NAME, null, null, null);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        reviewTaskService.reviewTask(reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // when
        reviewTaskService.cancelReviewedTask(reviewTask.getId());

        // then
        assertThat(reviewDateTaskRepository.findAll()).isEmpty();
    }

    @Test
    void 과제를_복습할_때_복습할_과제가_존재하지_않으면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle);
        reviewCycleRepository.save(reviewCycle);

        // when then
        Long reviewCycleId = reviewCycle.getId();
        assertThatThrownBy(() -> reviewTaskService.reviewTask(noneExistId, reviewCycleId, START_DATE))
                .isInstanceOf(ReviewTaskNotFoundException.class);
    }

    @Test
    void 과제를_복습할_때_복습_주기가_존재하지_않으면_에러가_발생한다() {
        // given
        Long noneExistId = 1L;

        ReviewTask reviewTask = new ReviewTask(REVIEW_TASK_NAME, null, null, null);
        reviewTaskRepository.save(reviewTask);

        // when then
        Long reviewTaskId = reviewTask.getId();
        assertThatThrownBy(() -> reviewTaskService.reviewTask(reviewTaskId, noneExistId, START_DATE))
                .isInstanceOf(ReviewCycleNotFoundException.class);
    }

}

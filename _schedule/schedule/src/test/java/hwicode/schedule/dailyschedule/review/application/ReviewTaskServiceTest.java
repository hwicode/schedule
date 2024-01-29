package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCancellationCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCommand;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewCycleNotFoundException;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewTaskNotFoundException;
import hwicode.schedule.dailyschedule.review.exception.domain.review_cycle.ReviewCycleForbiddenException;
import hwicode.schedule.dailyschedule.review.exception.domain.review_task.ReviewTaskForbiddenException;
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
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, userId);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewCommand command = new TaskReviewCommand(userId, reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // when
        reviewTaskService.reviewTask(command);

        // then
        assertThat(reviewDateTaskRepository.findAll()).hasSize(cycle.size());
    }

    @Test
    void 복습_주기에_맞춰_과제를_복습할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, userId);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewCommand command = new TaskReviewCommand(2L, reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // when then
        assertThatThrownBy(() -> reviewTaskService.reviewTask(command))
                .isInstanceOf(ReviewTaskForbiddenException.class);
    }

    @Test
    void 복습_주기에_맞춰_과제를_복습할_때_과제의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, 2L);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewCommand command = new TaskReviewCommand(userId, reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // when then
        assertThatThrownBy(() -> reviewTaskService.reviewTask(command))
                .isInstanceOf(ReviewTaskForbiddenException.class);
    }

    @Test
    void 복습_주기에_맞춰_과제를_복습할_때_복습_주기의_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, userId);
        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, 2L);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewCommand command = new TaskReviewCommand(userId, reviewTask.getId(), reviewCycle.getId(), START_DATE);

        // when then
        assertThatThrownBy(() -> reviewTaskService.reviewTask(command))
                .isInstanceOf(ReviewCycleForbiddenException.class);
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
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, userId);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewCommand reviewCommand = new TaskReviewCommand(userId, reviewTask.getId(), reviewCycle.getId(), START_DATE);

        reviewTaskService.reviewTask(reviewCommand);

        TaskReviewCancellationCommand command = new TaskReviewCancellationCommand(userId, reviewTask.getId());

        // when
        reviewTaskService.cancelReviewedTask(command);

        // then
        assertThat(reviewDateTaskRepository.findAll()).isEmpty();
    }

    @Test
    void 과제의_복습을_취소할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, userId);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, List.of(1, 2), userId);

        reviewTaskRepository.save(reviewTask);
        reviewCycleRepository.save(reviewCycle);

        TaskReviewCommand reviewCommand = new TaskReviewCommand(userId, reviewTask.getId(), reviewCycle.getId(), START_DATE);

        reviewTaskService.reviewTask(reviewCommand);

        TaskReviewCancellationCommand command = new TaskReviewCancellationCommand(2L, reviewTask.getId());

        // when then
        assertThatThrownBy(() -> reviewTaskService.cancelReviewedTask(command))
                .isInstanceOf(ReviewTaskForbiddenException.class);
    }

    @Test
    void 과제를_복습할_때_복습할_과제가_존재하지_않으면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Long noneExistId = 1L;

        List<Integer> cycle = List.of(1, 2, 4);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, cycle, userId);
        reviewCycleRepository.save(reviewCycle);

        // when then
        Long reviewCycleId = reviewCycle.getId();
        TaskReviewCommand command = new TaskReviewCommand(userId, noneExistId, reviewCycleId, START_DATE);

        assertThatThrownBy(() -> reviewTaskService.reviewTask(command))
                .isInstanceOf(ReviewTaskNotFoundException.class);
    }

    @Test
    void 과제를_복습할_때_복습_주기가_존재하지_않으면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Long noneExistId = 1L;

        ReviewTask reviewTask = new ReviewTask(null, REVIEW_TASK_NAME, null, null, null, userId);
        reviewTaskRepository.save(reviewTask);

        // when then
        Long reviewTaskId = reviewTask.getId();
        TaskReviewCommand command = new TaskReviewCommand(userId, reviewTaskId, noneExistId, START_DATE);

        assertThatThrownBy(() -> reviewTaskService.reviewTask(command))
                .isInstanceOf(ReviewCycleNotFoundException.class);
    }

    @Test
    void 과제의_복습을_취소할_때_복습할_과제가_존재하지_않으면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Long noneExistId = 1L;

        TaskReviewCancellationCommand command = new TaskReviewCancellationCommand(userId, noneExistId);

        // when then
        assertThatThrownBy(() -> reviewTaskService.cancelReviewedTask(command))
                .isInstanceOf(ReviewTaskNotFoundException.class);
    }

}

package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.common.login.validator.OwnerForbiddenException;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCommand;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewList;
import hwicode.schedule.dailyschedule.review.domain.ReviewSubTask;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewListNotFoundException;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewListRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewSubTaskRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class ReviewListServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    ReviewListService reviewListService;

    @Autowired
    ReviewTaskService reviewTaskService;

    @Autowired
    ReviewListRepository reviewListRepository;

    @Autowired
    ReviewTaskRepository reviewTaskRepository;

    @Autowired
    ReviewSubTaskRepository reviewSubTaskRepository;

    @Autowired
    ReviewCycleRepository reviewCycleRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 5, 7, 10})
    void 복습_리스트에_복습할_과제들을_추가할_수_있다(int numberOfTasks) {
        // given
        Long userId = 1L;
        ReviewList reviewList = new ReviewList(START_DATE.plusDays(1), userId);
        reviewListRepository.save(reviewList);

        // 복습 주기를 하루로 해서, 다음날에 복습할 과제만 추가하도록 만듦
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, List.of(1), userId);
        reviewCycleRepository.save(reviewCycle);

        int numberOfReviewedTasks = createReviewedTasks(reviewCycle, numberOfTasks, userId);

        // when
        reviewListService.addReviewTasks(userId, reviewList.getId());

        // then
        assertThat(reviewTaskRepository.findAll()).hasSize(numberOfTasks + numberOfReviewedTasks);
        assertThat(reviewSubTaskRepository.findAll()).hasSize(numberOfTasks * 2 + numberOfReviewedTasks * 2);
    }

    private int createReviewedTasks(ReviewCycle reviewCycle, int numberOfTasks, Long userId) {
        for (int i = 0; i < numberOfTasks; i++) {
            ReviewTask task = createTask(REVIEW_TASK_NAME + i, userId);

            TaskReviewCommand command = new TaskReviewCommand(userId, task.getId(), reviewCycle.getId(), START_DATE);
            reviewTaskService.reviewTask(command);
        }
        return numberOfTasks;
    }

    private ReviewTask createTask(String reviewTaskName, Long userId) {
        ReviewTask reviewTask = new ReviewTask(null, reviewTaskName, null, null, null, userId);
        reviewTaskRepository.save(reviewTask);
        reviewSubTaskRepository.save(new ReviewSubTask(reviewTask, REVIEW_SUB_TASK_NAME, userId));
        reviewSubTaskRepository.save(new ReviewSubTask(reviewTask, REVIEW_SUB_TASK_NAME + 2, userId));
        return reviewTask;
    }

    @Test
    void 존재하지_않는_복습_리스트를_조회하면_에러가_발생한다() {
        // given
        Long userId = 1L;
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> reviewListService.addReviewTasks(userId, noneExistId))
                .isInstanceOf(ReviewListNotFoundException.class);
    }

    @Test
    void 복습_리스트에_복습할_과제들을_추가할_때_소유자가_아니라면_에러가_발생한다() {
        // given
        Long userId = 1L;
        ReviewList reviewList = new ReviewList(START_DATE.plusDays(1), userId);
        reviewListRepository.save(reviewList);

        // when then
        Long reviewListId = reviewList.getId();
        assertThatThrownBy(() -> reviewListService.addReviewTasks(2L, reviewListId))
                .isInstanceOf(OwnerForbiddenException.class);
    }

}

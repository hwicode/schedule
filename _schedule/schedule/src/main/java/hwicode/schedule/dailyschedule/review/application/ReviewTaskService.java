package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.common.login.validator.PermissionValidator;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCancellationCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCommand;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import hwicode.schedule.dailyschedule.review.domain.ReviewDateTask;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewCycleNotFoundException;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewTaskNotFoundException;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewTaskRepository;
import hwicode.schedule.dailyschedule.review.infra.limited_repository.ReviewDateTaskSaveAllOrDeleteAllRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewTaskService {

    private final ReviewTaskRepository reviewTaskRepository;
    private final ReviewCycleRepository reviewCycleRepository;

    private final ReviewDateProviderService reviewDateProviderService;
    private final ReviewDateTaskSaveAllOrDeleteAllRepository reviewDateTaskSaveAllOrDeleteAllRepository;

    @Transactional
    public Long reviewTask(TaskReviewCommand command) {
        ReviewTask reviewTask = reviewTaskRepository.findReviewTaskWithReviewDateTasks(command.getReviewTaskId())
                .orElseThrow(ReviewTaskNotFoundException::new);
        PermissionValidator.validateOwnership(command.getUserId(), reviewTask.getUserId());

        ReviewCycle reviewCycle = reviewCycleRepository.findById(command.getReviewCycleId())
                .orElseThrow(ReviewCycleNotFoundException::new);
        PermissionValidator.validateOwnership(command.getUserId(), reviewCycle.getUserId());

        List<ReviewDate> reviewDates = reviewDateProviderService.provideReviewDates(reviewCycle, command.getStartDate());

        List<ReviewDateTask> reviewDateTasks = reviewTask.review(reviewDates);
        reviewDateTaskSaveAllOrDeleteAllRepository.saveAll(reviewDateTasks);
        return reviewTask.getId();
    }

    @Transactional
    public Long cancelReviewedTask(TaskReviewCancellationCommand command) {
        ReviewTask reviewTask = reviewTaskRepository.findById(command.getReviewTaskId())
                .orElseThrow(ReviewTaskNotFoundException::new);
        PermissionValidator.validateOwnership(command.getUserId(), reviewTask.getUserId());

        reviewDateTaskSaveAllOrDeleteAllRepository.deleteAllReviewDateTasksBy(command.getReviewTaskId());
        return command.getReviewTaskId();
    }

}

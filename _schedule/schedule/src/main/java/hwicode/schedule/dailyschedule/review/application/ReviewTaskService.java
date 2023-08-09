package hwicode.schedule.dailyschedule.review.application;

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

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewTaskService {

    private final ReviewTaskRepository reviewTaskRepository;
    private final ReviewCycleRepository reviewCycleRepository;

    private final ReviewDateProviderService reviewDateProviderService;
    private final ReviewDateTaskSaveAllOrDeleteAllRepository reviewDateTaskSaveAllOrDeleteAllRepository;

    @Transactional
    public void reviewTask(Long reviewTaskId, Long reviewCycleId, LocalDate startDate) {
        ReviewTask reviewTask = reviewTaskRepository.findReviewTaskWithReviewDateTasks(reviewTaskId)
                .orElseThrow(ReviewTaskNotFoundException::new);
        ReviewCycle reviewCycle = reviewCycleRepository.findById(reviewCycleId)
                .orElseThrow(ReviewCycleNotFoundException::new);
        List<ReviewDate> reviewDates = reviewDateProviderService.provideReviewDates(reviewCycle, startDate);

        List<ReviewDateTask> reviewDateTasks = reviewTask.review(reviewDates);
        reviewDateTaskSaveAllOrDeleteAllRepository.saveAll(reviewDateTasks);
    }

    @Transactional
    public void cancelReviewedTask(Long reviewTaskId) {
        reviewDateTaskSaveAllOrDeleteAllRepository.deleteAllReviewDateTasksBy(reviewTaskId);
    }

}

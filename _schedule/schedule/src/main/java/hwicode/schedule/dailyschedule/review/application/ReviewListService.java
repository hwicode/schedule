package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import hwicode.schedule.dailyschedule.review.domain.ReviewDateTask;
import hwicode.schedule.dailyschedule.review.domain.ReviewList;
import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewListNotFoundException;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewListRepository;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewListService {

    private final ReviewDateProviderService reviewDateProviderService;

    private final ReviewListRepository reviewListRepository;
    private final ReviewDateTaskRepository reviewDateTaskRepository;
    private final ReviewTaskRepository reviewTaskRepository;

    @Transactional
    public void addReviewTasks(Long reviewListId) {
        ReviewList reviewList = reviewListRepository.findById(reviewListId)
                .orElseThrow(ReviewListNotFoundException::new);

        ReviewDate reviewDate = reviewDateProviderService.provideReviewDate(reviewList.getToday());
        List<ReviewDateTask> reviewDateTasks = reviewDateTaskRepository.findAllReviewDateTaskBy(reviewDate.getId());

        List<ReviewTask> clonedReviewTasks = reviewDateTasks.stream()
                .map(ReviewDateTask::getReviewTask)
                .map(reviewTask -> reviewTask.cloneTask(reviewList))
                .collect(Collectors.toList());

        reviewTaskRepository.saveAll(clonedReviewTasks);
    }

}

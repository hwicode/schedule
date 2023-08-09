package hwicode.schedule.dailyschedule.review.infra.limited_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDateTask;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewDateTaskSaveAllOrDeleteAllRepository {

    private final ReviewDateTaskRepository reviewDateTaskRepository;

    public List<ReviewDateTask> saveAll(List<ReviewDateTask> reviewDateTasks) {
        return reviewDateTaskRepository.saveAll(reviewDateTasks);
    }

    public void deleteAllReviewDateTasksBy(Long reviewTaskId) {
        reviewDateTaskRepository.deleteAllReviewDateTasksBy(reviewTaskId);
    }
}

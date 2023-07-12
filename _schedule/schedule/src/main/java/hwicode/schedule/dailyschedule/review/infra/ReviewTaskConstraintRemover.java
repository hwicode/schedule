package hwicode.schedule.dailyschedule.review.infra;

import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewTaskConstraintRemover {

    private final ReviewDateTaskRepository reviewDateTaskRepository;

    public void deleteReviewTaskConstraint(Long reviewTaskId) {
        reviewDateTaskRepository.deleteAllReviewDateTasksBy(reviewTaskId);
    }
}

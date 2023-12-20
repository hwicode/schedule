package hwicode.schedule.dailyschedule.review.infra.other_boundedcontext;

import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.TaskConstraintRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReviewTaskConstraintRemover implements TaskConstraintRemover {

    private final ReviewDateTaskRepository reviewDateTaskRepository;

    @Override
    public void delete(Long taskId) {
        reviewDateTaskRepository.deleteAllReviewDateTasksBy(taskId);
    }
}

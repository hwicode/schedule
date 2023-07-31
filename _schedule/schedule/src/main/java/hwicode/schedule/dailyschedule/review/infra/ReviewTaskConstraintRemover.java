package hwicode.schedule.dailyschedule.review.infra;

import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateTaskRepository;
import hwicode.schedule.dailyschedule.todolist.infra.service_impl.TaskConstraintRemover;
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

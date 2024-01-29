package hwicode.schedule.dailyschedule.review.presentation.reviewtask;

import hwicode.schedule.dailyschedule.review.application.ReviewTaskService;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCancellationCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_task.TaskReviewCommand;
import hwicode.schedule.dailyschedule.review.presentation.reviewtask.dto.TaskReviewRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewtask.dto.TaskReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class ReviewTaskController {

    private final ReviewTaskService reviewTaskService;

    @PostMapping("/dailyschedule/tasks/{taskId}/review")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskReviewResponse reviewTask(@PathVariable("taskId") @Positive Long reviewTaskId,
                                         @RequestBody @Valid TaskReviewRequest request) {
        TaskReviewCommand command = new TaskReviewCommand(
                1L, reviewTaskId, request.getReviewCycleId(), request.getStartDate()
        );
        Long id = reviewTaskService.reviewTask(command);
        return new TaskReviewResponse(id);
    }

    @DeleteMapping("/dailyschedule/tasks/{taskId}/review")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelReviewedTask(@PathVariable("taskId") @Positive Long reviewTaskId) {
        TaskReviewCancellationCommand command = new TaskReviewCancellationCommand(1L, reviewTaskId);
        reviewTaskService.cancelReviewedTask(command);
    }

}

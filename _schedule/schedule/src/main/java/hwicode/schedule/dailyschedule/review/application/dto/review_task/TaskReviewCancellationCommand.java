package hwicode.schedule.dailyschedule.review.application.dto.review_task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskReviewCancellationCommand {

    private final Long userId;
    private final Long reviewTaskId;
}

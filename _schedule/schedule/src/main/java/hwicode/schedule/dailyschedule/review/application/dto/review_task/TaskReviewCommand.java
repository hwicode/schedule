package hwicode.schedule.dailyschedule.review.application.dto.review_task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class TaskReviewCommand {

    private final Long userId;
    private final Long reviewTaskId;
    private final Long reviewCycleId;
    private final LocalDate startDate;
}

package hwicode.schedule.dailyschedule.review.application.dto.review_cycle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewCycleDeleteCommand {

    private final Long userId;
    private final Long reviewCycleId;
}

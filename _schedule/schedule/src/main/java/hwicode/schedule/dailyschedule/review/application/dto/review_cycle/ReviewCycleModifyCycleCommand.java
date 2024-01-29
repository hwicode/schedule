package hwicode.schedule.dailyschedule.review.application.dto.review_cycle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewCycleModifyCycleCommand {

    private final Long userId;
    private final Long reviewCycleId;
    private final List<Integer> cycle;
}

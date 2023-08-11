package hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewCycleCycleModifyResponse {

    private Long reviewCycleId;
    private List<Integer> cycle;
}

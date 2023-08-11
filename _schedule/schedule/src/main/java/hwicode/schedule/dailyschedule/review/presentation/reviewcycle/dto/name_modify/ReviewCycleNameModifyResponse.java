package hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCycleNameModifyResponse {

    private Long reviewCycleId;
    private String newReviewCycleName;
}

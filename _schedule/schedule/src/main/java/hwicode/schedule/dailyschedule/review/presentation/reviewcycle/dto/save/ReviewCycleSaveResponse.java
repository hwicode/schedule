package hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewCycleSaveResponse {

    private Long reviewCycleId;
    private String reviewCycleName;
    private List<Integer> cycle;
}

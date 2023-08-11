package hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReviewCycleCycleModifyRequest {

    @NotEmpty
    @Size(min = 1, max = 60, message = "Review cycle is greater than or equal to 1 and less than or equal to 60 (1 <= review cycle <= 60)")
    private List<Integer> cycle;
}

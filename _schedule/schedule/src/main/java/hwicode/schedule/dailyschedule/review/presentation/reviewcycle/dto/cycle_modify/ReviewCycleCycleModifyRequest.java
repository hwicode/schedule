package hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReviewCycleCycleModifyRequest {

    @NotEmpty
    @Size(min = 1, max = 60, message = "Review cycle is greater than or equal to 1 and less than or equal to 60 (1 <= review cycle <= 60)")
    private Set<Integer> cycle;

    public List<Integer> getCycle() {
        List<Integer> list = new ArrayList<>(cycle);
        Collections.sort(list);
        return list;
    }
}

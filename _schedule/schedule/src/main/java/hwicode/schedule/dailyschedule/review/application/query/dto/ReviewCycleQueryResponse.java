package hwicode.schedule.dailyschedule.review.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class ReviewCycleQueryResponse {

    private final Long id;
    private final String name;
    private final List<Integer> reviewCycleDates;
}

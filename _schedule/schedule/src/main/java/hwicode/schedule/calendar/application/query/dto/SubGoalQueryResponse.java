package hwicode.schedule.calendar.application.query.dto;

import hwicode.schedule.calendar.domain.SubGoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class SubGoalQueryResponse {

    private final Long id;
    private final String name;
    private final SubGoalStatus subGoalStatus;
    private final Long goalId;
}

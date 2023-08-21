package hwicode.schedule.calendar.application.query.dto;

import hwicode.schedule.calendar.domain.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class GoalQueryResponse {

    private final Long id;
    private final String name;
    private final GoalStatus goalStatus;

    private List<SubGoalQueryResponse> subGoalResponses;

    public GoalQueryResponse(Long id, String name, GoalStatus goalStatus) {
        this.id = id;
        this.name = name;
        this.goalStatus = goalStatus;
    }
}

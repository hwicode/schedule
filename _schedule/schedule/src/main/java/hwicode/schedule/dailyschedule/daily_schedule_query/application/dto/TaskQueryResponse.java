package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.*;

import java.util.List;

@Builder
@Data
public class TaskQueryResponse {

    private final Long id;
    private final String name;
    private final Priority priority;
    private final Importance importance;
    private final Difficulty difficulty;
    private final TaskStatus taskStatus;

    private List<SubTaskQueryResponse> subTaskQueryResponses;
}

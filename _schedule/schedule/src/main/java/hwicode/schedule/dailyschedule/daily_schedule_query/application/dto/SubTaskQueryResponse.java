package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.*;

@Builder
@Data
public class SubTaskQueryResponse {

    private final Long id;
    private final String name;
    private final SubTaskStatus subTaskStatus;
    private final Long taskId;
}

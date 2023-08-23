package hwicode.schedule.dailyschedule.daily_schedule_query.application.dto;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.*;

@Data
public class SubTaskQueryResponse {

    private final Long id;
    private final String name;
    private final SubTaskStatus subTaskStatus;
    private final Long taskId;

    @Builder
    public SubTaskQueryResponse(Long id, String name, SubTaskStatus subTaskStatus, Long taskId) {
        this.id = id;
        this.name = name;
        this.subTaskStatus = subTaskStatus;
        this.taskId = taskId;
    }
}

package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.delete;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDeleteRequest {

    private Long dailyChecklistId;

    public TaskDeleteRequest(Long dailyChecklistId) {
        this.dailyChecklistId = dailyChecklistId;
    }
}

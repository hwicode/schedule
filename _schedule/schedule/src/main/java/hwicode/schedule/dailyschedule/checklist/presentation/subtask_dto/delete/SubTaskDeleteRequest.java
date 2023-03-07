package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubTaskDeleteRequest {

    private Long dailyChecklistId;
    private String taskName;

    public SubTaskDeleteRequest(Long dailyChecklistId, String taskName) {
        this.dailyChecklistId = dailyChecklistId;
        this.taskName = taskName;
    }
}

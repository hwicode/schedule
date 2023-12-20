package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskDeleteRequest {

    private Long dailyChecklistId;
    private String taskName;
    private Long subTaskId;
    private String subTaskName;
}

package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubTaskSaveResponse {

    private Long subTaskId;
    private String subTaskName;

    public SubTaskSaveResponse(Long subTaskId, String subTaskName) {
        this.subTaskId = subTaskId;
        this.subTaskName = subTaskName;
    }
}

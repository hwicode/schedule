package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskSaveResponse {

    private Long subTaskId;
    private String subTaskName;
}

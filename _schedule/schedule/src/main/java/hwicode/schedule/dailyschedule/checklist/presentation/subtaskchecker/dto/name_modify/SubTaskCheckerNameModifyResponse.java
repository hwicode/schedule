package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskCheckerNameModifyResponse {

    private Long taskId;
    private String newSubTaskName;
}

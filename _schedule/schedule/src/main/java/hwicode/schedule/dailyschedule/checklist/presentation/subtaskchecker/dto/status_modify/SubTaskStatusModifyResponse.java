package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskStatusModifyResponse {

    private String subTaskName;
    private TaskStatus modifiedTaskStatus;
    private SubTaskStatus modifiedSubTaskStatus;
}

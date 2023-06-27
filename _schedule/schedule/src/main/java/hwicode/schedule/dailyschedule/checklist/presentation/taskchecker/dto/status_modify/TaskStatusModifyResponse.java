package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify;

import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskStatusModifyResponse {

    private String taskCheckerName;
    private TaskStatus modifiedTaskStatus;
}

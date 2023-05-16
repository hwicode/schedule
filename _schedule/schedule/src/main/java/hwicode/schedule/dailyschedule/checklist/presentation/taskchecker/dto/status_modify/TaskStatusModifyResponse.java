package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskStatusModifyResponse {

    private String taskCheckerName;
    private TaskStatus modifiedTaskStatus;
}

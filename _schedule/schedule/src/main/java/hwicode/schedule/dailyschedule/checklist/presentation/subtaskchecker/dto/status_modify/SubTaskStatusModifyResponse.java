package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskStatusModifyResponse {

    private String subTaskCheckerName;
    private TaskStatus modifiedTaskStatus;
    private SubTaskStatus modifiedSubTaskStatus;
}

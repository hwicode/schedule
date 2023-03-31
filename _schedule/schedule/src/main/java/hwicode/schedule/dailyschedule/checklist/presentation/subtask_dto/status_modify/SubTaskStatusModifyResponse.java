package hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubTaskStatusModifyResponse {

    @NotBlank
    private String subTaskName;

    @NotNull
    private TaskStatus modifiedTaskStatus;

    @NotNull
    private SubTaskStatus modifiedSubTaskStatus;

    public SubTaskStatusModifyResponse(String subTaskName, TaskStatus modifiedTaskStatus, SubTaskStatus modifiedSubTaskStatus) {
        this.subTaskName = subTaskName;
        this.modifiedTaskStatus = modifiedTaskStatus;
        this.modifiedSubTaskStatus = modifiedSubTaskStatus;
    }
}

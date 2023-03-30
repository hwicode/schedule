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
    private TaskStatus taskStatus;

    @NotNull
    private SubTaskStatus subTaskStatus;

    public SubTaskStatusModifyResponse(String subTaskName, TaskStatus taskStatus, SubTaskStatus subTaskStatus) {
        this.subTaskName = subTaskName;
        this.taskStatus = taskStatus;
        this.subTaskStatus = subTaskStatus;
    }
}

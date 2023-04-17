package hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskStatusModifyResponse {

    @NotBlank
    private String subTaskCheckerName;

    @NotNull
    private TaskStatus modifiedTaskStatus;

    @NotNull
    private SubTaskStatus modifiedSubTaskStatus;
}

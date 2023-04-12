package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify;

import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskStatusModifyResponse {

    @NotBlank
    private String taskCheckerName;

    @NotNull
    private TaskStatus modifiedTaskStatus;
}

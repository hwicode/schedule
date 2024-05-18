package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskStatusModifyRequest {

    @NotBlank
    private String taskName;

    @NotBlank
    private String subTaskName;

    @NotNull
    private SubTaskStatus subTaskStatus;
}

package hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify;

import hwicode.schedule.dailyschedule.common.domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskStatusModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskCheckerName;

    @NotNull
    private SubTaskStatus subTaskStatus;
}

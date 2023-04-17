package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.name_modify;

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
public class TaskCheckerNameModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String newTaskCheckerName;
}

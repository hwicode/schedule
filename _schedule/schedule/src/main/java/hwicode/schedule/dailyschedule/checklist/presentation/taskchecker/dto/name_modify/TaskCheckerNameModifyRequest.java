package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify;

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
    private String taskCheckerName;

    @NotBlank
    private String newTaskCheckerName;
}

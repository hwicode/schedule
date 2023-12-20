package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save;

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
public class SubTaskSaveRequest {

    @NotNull
    @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    @NotBlank
    private String subTaskName;
}

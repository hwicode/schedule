package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskSaveRequest {

    @NotBlank
    private String taskName;

    @NotBlank
    private String subTaskName;
}

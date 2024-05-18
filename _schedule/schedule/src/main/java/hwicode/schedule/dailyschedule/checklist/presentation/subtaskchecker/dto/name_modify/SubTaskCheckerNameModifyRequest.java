package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskCheckerNameModifyRequest {

    @NotBlank
    private String subTaskName;

    @NotBlank
    private String newSubTaskName;
}

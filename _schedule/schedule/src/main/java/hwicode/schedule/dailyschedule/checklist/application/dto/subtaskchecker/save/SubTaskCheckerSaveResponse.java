package hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.save;

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
public class SubTaskCheckerSaveResponse {

    @NotNull @Positive
    private Long subTaskId;

    @NotBlank
    private String subTaskCheckerName;
}
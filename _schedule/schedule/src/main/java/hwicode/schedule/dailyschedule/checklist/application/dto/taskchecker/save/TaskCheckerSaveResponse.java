package hwicode.schedule.dailyschedule.checklist.application.dto.taskchecker.save;

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
public class TaskCheckerSaveResponse {

    @NotNull @Positive
    private Long taskId;

    @NotBlank
    private String taskCheckerName;
}

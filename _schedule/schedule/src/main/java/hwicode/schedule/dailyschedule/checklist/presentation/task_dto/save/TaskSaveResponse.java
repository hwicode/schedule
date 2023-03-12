package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class TaskSaveResponse {

    @NotNull @Positive
    private Long taskId;

    @NotBlank
    private String taskName;

    public TaskSaveResponse(Long taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }
}

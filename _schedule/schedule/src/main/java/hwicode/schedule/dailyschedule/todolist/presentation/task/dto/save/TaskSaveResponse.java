package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save;

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
public class TaskSaveResponse {

    @NotNull @Positive
    private Long taskId;

    @NotBlank
    private String taskName;
}

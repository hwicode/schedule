package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskDeleteRequest {

    @NotNull @Positive
    private Long dailyToDoListId;

    @NotNull @Positive
    private Long taskId;
}

package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.name_modify;

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
public class TaskNameModifyResponse {

    @NotNull @Positive
    private Long dailyToDoListId;

    @NotBlank
    private String newTaskName;
}

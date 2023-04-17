package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save;

import hwicode.schedule.dailyschedule.dailyschedule_domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
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
public class TaskSaveRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskName;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Priority priority;

    @NotNull
    private Importance importance;
}

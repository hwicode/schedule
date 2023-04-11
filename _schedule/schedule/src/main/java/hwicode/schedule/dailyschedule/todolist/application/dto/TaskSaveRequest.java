package hwicode.schedule.dailyschedule.todolist.application.dto;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskSaveRequest {

    @NotBlank
    private String taskName;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Priority priority;

    @NotNull
    private Importance importance;
}

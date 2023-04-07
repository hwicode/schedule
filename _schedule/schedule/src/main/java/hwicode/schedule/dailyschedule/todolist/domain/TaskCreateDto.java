package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCreateDto {

    private final String taskName;
    private final Difficulty difficulty;
    private final Priority priority;
    private final Importance importance;
}

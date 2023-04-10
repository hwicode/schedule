package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskCreateDto {

    private final String taskName;
    private final Difficulty difficulty;
    private final Priority priority;
    private final Importance importance;
}

package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskSaveResponse {

    private Long taskId;
    private String taskName;
}

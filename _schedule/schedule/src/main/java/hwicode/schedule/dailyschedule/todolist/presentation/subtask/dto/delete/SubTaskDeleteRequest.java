package hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskDeleteRequest {

    private Long dailyToDoListId;
    private String taskName;
}

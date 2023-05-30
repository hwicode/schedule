package hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskSaveResponse {

    private Long subTaskId;
    private String subTaskName;
}

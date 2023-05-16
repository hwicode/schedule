package hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskSaveResponse {

    private Long subTaskId;
    private String subTaskName;
}

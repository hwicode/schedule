package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSaveResponse {

    private Long taskId;
    private String taskName;
}

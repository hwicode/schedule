package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCheckerNameModifyResponse {

    private Long dailyToDoListId;
    private String newTaskName;
}

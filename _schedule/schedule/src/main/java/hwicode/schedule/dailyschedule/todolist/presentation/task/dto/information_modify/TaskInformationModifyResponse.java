package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify;

import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskInformationModifyResponse {

    private Long taskId;
    private Priority modifiedPriority;
    private Importance modifiedImportance;
}

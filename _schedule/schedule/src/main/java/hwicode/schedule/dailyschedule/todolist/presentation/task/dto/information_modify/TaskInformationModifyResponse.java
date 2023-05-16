package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify;

import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskInformationModifyResponse {

    private Long taskId;
    private Priority modifiedPriority;
    private Importance modifiedImportance;
}

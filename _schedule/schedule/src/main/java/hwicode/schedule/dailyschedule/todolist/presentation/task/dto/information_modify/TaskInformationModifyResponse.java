package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify;

import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskInformationModifyResponse {

    @NotNull
    private Long taskId;

    @NotNull
    private Priority modifiedPriority;

    @NotNull
    private Importance modifiedImportance;
}

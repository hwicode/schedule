package hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify;

import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskInformationModifyRequest {

    @NotNull
    private Priority priority;

    @NotNull
    private Importance importance;
}

package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify;

import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskStatusModifyRequest {

    @NotNull @Positive
    private Long dailyToDoListId;

    @NotBlank
    private String taskName;

    @NotNull
    private TaskStatus taskStatus;
}

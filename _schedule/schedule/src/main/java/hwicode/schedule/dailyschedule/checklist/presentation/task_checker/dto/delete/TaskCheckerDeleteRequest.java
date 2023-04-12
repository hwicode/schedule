package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.delete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskCheckerDeleteRequest {

    @NotNull @Positive
    private Long dailyChecklistId;
}

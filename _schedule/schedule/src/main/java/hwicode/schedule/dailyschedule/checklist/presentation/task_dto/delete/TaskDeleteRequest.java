package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.delete;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskDeleteRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    public TaskDeleteRequest(Long dailyChecklistId) {
        this.dailyChecklistId = dailyChecklistId;
    }
}

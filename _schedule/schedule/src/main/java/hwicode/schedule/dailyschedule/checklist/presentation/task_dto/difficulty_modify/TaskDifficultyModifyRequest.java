package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class TaskDifficultyModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotNull
    private Difficulty difficulty;

    public TaskDifficultyModifyRequest(Long dailyChecklistId, Difficulty difficulty) {
        this.dailyChecklistId = dailyChecklistId;
        this.difficulty = difficulty;
    }
}

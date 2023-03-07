package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDifficultyModifyRequest {

    private Long dailyChecklistId;
    private Difficulty difficulty;

    public TaskDifficultyModifyRequest(Long dailyChecklistId, Difficulty difficulty) {
        this.dailyChecklistId = dailyChecklistId;
        this.difficulty = difficulty;
    }
}

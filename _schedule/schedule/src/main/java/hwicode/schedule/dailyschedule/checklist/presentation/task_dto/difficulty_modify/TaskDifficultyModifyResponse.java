package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDifficultyModifyResponse {

    private String taskName;
    private Difficulty modifiedDifficulty;

    public TaskDifficultyModifyResponse(String taskName, Difficulty modifiedDifficulty) {
        this.taskName = taskName;
        this.modifiedDifficulty = modifiedDifficulty;
    }
}

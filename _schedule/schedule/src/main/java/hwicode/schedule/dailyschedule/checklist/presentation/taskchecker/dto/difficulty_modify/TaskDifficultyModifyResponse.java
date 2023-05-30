package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskDifficultyModifyResponse {

    private String taskCheckerName;
    private Difficulty modifiedDifficulty;
}

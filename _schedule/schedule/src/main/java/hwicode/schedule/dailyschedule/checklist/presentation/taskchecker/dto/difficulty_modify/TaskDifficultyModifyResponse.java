package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskDifficultyModifyResponse {

    private String taskCheckerName;
    private Difficulty modifiedDifficulty;
}

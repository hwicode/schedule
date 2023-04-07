package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskDifficultyModifyResponse {

    @NotBlank
    private String taskName;

    @NotNull
    private Difficulty modifiedDifficulty;

    public TaskDifficultyModifyResponse(String taskName, Difficulty modifiedDifficulty) {
        this.taskName = taskName;
        this.modifiedDifficulty = modifiedDifficulty;
    }
}

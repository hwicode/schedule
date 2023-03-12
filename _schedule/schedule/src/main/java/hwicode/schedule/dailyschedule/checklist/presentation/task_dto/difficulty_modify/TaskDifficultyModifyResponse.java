package hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
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

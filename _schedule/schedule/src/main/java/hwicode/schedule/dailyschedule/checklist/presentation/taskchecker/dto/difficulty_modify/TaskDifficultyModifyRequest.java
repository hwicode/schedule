package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskDifficultyModifyRequest {

    @NotBlank
    private String taskName;

    @NotNull
    private Difficulty difficulty;
}

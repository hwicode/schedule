package hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskDifficultyModifyRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotNull
    private Difficulty difficulty;
}

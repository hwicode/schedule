package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.taskchecker.save;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskCheckerSaveRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskCheckerName;

    @NotNull
    private Difficulty difficulty;
}

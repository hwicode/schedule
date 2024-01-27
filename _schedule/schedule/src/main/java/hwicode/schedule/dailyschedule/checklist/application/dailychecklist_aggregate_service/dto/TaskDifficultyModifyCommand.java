package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskDifficultyModifyCommand {

    private final Long userId;
    private final Long dailyChecklistId;
    private final String taskCheckerName;
    private final Difficulty difficulty;
}

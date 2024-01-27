package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskCheckerNameModifyCommand {

    private final Long userId;
    private final Long dailyChecklistId;
    private final String taskCheckerName;
    private final String newTaskCheckerName;
}

package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto;

import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskStatusModifyCommand {

    private final Long userId;
    private final Long dailyChecklistId;
    private final String taskCheckerName;
    private final TaskStatus taskStatus;
}

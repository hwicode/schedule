package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskSaveCommand {

    private final Long userId;
    private final Long dailyChecklistId;
    private final String taskCheckerName;
    private final Difficulty difficulty;
    private final Priority priority;
    private final Importance importance;
}

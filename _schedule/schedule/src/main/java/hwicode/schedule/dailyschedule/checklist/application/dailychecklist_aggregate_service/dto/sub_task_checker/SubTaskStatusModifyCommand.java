package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubTaskStatusModifyCommand {

    private final Long userId;
    private final Long dailyChecklistId;
    private final String taskCheckerName;
    private final String subTaskCheckerName;
    private final SubTaskStatus subTaskStatus;
}

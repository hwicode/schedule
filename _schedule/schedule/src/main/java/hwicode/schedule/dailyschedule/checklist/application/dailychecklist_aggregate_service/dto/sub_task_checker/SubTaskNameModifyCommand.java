package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubTaskNameModifyCommand {

    private final Long userId;
    private final Long taskCheckerId;
    private final String subTaskCheckerName;
    private final String newSubTaskCheckerName;
}

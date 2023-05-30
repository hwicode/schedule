package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCheckerSaveRequest {

    private Long dailyChecklistId;
    private String taskCheckerName;
    private Difficulty difficulty;
}

package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubTaskCheckerDeleteRequest {

    private Long dailyChecklistId;
    private String taskCheckerName;
}

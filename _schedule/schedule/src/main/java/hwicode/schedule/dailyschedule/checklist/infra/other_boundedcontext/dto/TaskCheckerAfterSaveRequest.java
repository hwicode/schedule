package hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.dto;

import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCheckerAfterSaveRequest {

    private final Long taskCheckerId;
    private final Priority priority;
    private final Importance importance;
    private final Long userId;
}

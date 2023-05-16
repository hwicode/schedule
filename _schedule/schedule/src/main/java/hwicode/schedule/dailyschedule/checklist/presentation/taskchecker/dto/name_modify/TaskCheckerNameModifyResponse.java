package hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaskCheckerNameModifyResponse {

    private Long dailyChecklistId;
    private String newTaskCheckerName;
}

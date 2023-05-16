package hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskCheckerNameModifyResponse {

    private Long taskCheckerId;
    private String newSubTaskCheckerName;
}

package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.save;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SubTaskCheckerSaveRequest {

    @NotNull @Positive
    private Long dailyChecklistId;

    @NotBlank
    private String taskCheckerName;

    @NotBlank
    private String subTaskCheckerName;

    public SubTaskChecker toEntity() {
        return new SubTaskChecker(subTaskCheckerName);
    }
}

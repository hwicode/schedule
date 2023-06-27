package hwicode.schedule.timetable.presentation.learningtime.dto.subjectoftask_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LearningTimeSubjectOfTaskModifyRequest {

    @NotNull @Positive
    private Long subjectOfTaskId;
}

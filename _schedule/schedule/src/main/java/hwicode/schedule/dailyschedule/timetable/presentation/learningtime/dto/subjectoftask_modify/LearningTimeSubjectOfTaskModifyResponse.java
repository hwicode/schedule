package hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify;

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
public class LearningTimeSubjectOfTaskModifyResponse {

    @NotNull @Positive
    private Long learningTimeId;

    @NotBlank
    private String newSubject;
}

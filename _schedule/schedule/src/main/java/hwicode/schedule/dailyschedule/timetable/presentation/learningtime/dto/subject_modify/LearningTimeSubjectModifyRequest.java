package hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LearningTimeSubjectModifyRequest {

    @NotBlank
    private String newSubject;
}

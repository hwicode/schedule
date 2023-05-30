package hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LearningTimeSubjectModifyResponse {

    private Long learningTimeId;
    private String newSubject;
}

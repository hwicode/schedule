package hwicode.schedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LearningTimeSubjectOfSubTaskModifyResponse {

    private Long learningTimeId;
    private String newSubject;
}

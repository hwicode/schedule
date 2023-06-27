package hwicode.schedule.timetable.presentation.learningtime.dto.subjectoftask_modify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LearningTimeSubjectOfTaskModifyResponse {

    private Long learningTimeId;
    private String newSubject;
}

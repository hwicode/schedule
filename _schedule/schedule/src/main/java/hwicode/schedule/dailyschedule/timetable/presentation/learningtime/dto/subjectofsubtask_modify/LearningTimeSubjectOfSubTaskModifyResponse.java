package hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LearningTimeSubjectOfSubTaskModifyResponse {

    private Long learningTimeId;
    private String newSubject;
}

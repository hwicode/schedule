package hwicode.schedule.dailyschedule.timetable;

import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.dto.subject_modify.LearningTimeSubjectModifyResponse;

import java.time.LocalDateTime;

public class TimeTableDataHelper {

    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 4, 19, 5, 5);
    public static final LocalDateTime NEW_START_TIME = LocalDateTime.of(2023, 4, 19, 6, 6);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 4, 19, 6, 6);

    public static final String SUBJECT = "subject";
    public static final String NEW_SUBJECT = "newSubject";

    // LearningTime request dto
    public static LearningTimeSubjectModifyRequest createLearningTimeSubjectModifyRequest(String newSubject) {
        return new LearningTimeSubjectModifyRequest(newSubject);
    }

    // LearningTime response dto
    public static LearningTimeSubjectModifyResponse createLearningTimeSubjectModifyResponse(Long learningTimeId, String newSubject) {
        return new LearningTimeSubjectModifyResponse(learningTimeId, newSubject);
    }
}

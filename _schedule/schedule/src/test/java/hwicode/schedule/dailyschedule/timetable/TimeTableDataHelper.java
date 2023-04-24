package hwicode.schedule.dailyschedule.timetable;

import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.delete.LearningTimeDeleteRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;

import java.time.LocalDateTime;

public class TimeTableDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long TIME_TABLE_ID = 1L;
    public static final Long LEARNING_TIME_ID = 1L;
    public static final Long SUBJECT_OF_TASK_ID = 1L;
    public static final Long SUBJECT_OF_SUBTASK_ID = 1L;


    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 4, 19, 5, 5);
    public static final LocalDateTime NEW_START_TIME = LocalDateTime.of(2023, 4, 19, 6, 6);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2023, 4, 19, 6, 6);

    public static final String SUBJECT = "subject";
    public static final String NEW_SUBJECT = "newSubject";


    // learningtime package request dto
    public static LearningTimeSubjectModifyRequest createLearningTimeSubjectModifyRequest(String newSubject) {
        return new LearningTimeSubjectModifyRequest(newSubject);
    }

    public static LearningTimeSubjectOfTaskModifyRequest createLearningTimeSubjectOfTaskModifyRequest(Long subjectOfTaskId) {
        return new LearningTimeSubjectOfTaskModifyRequest(subjectOfTaskId);
    }

    public static LearningTimeSubjectOfSubTaskModifyRequest createLearningTimeSubjectOfSubTaskModifyRequest(Long subjectOfSubTaskId) {
        return new LearningTimeSubjectOfSubTaskModifyRequest(subjectOfSubTaskId);
    }


    // learningtime package response dto
    public static LearningTimeSubjectModifyResponse createLearningTimeSubjectModifyResponse(Long learningTimeId, String newSubject) {
        return new LearningTimeSubjectModifyResponse(learningTimeId, newSubject);
    }

    public static LearningTimeSubjectOfTaskModifyResponse createLearningTimeSubjectOfTaskModifyResponse(Long learningTimeId, String newSubject) {
        return new LearningTimeSubjectOfTaskModifyResponse(learningTimeId, newSubject);
    }

    public static LearningTimeSubjectOfSubTaskModifyResponse createLearningTimeSubjectOfSubTaskModifyResponse(Long learningTimeId, String newSubject) {
        return new LearningTimeSubjectOfSubTaskModifyResponse(learningTimeId, newSubject);
    }


    // timetable package request dto
    public static LearningTimeSaveRequest createLearningTimeSaveRequest(LocalDateTime startTime) {
        return new LearningTimeSaveRequest(startTime);
    }

    public static StartTimeModifyRequest createStartTimeModifyRequest(Long timeTableId, LocalDateTime newStartTime) {
        return new StartTimeModifyRequest(timeTableId, newStartTime);
    }

    public static EndTimeModifyRequest createEndTimeModifyRequest(Long timeTableId, LocalDateTime endTime) {
        return new EndTimeModifyRequest(timeTableId, endTime);
    }

    public static LearningTimeDeleteRequest createLearningTimeDeleteRequest(Long timeTableId) {
        return new LearningTimeDeleteRequest(timeTableId);
    }


    // timetalbe package response dto
    public static LearningTimeSaveResponse createLearningTimeSaveResponse(Long learningTimeId, LocalDateTime startTime) {
        return new LearningTimeSaveResponse(learningTimeId, startTime);
    }

    public static StartTimeModifyResponse createStartTimeModifyResponse(LocalDateTime newStartTime) {
        return new StartTimeModifyResponse(newStartTime);
    }

    public static EndTimeModifyResponse createEndTimeModifyResponse(LocalDateTime endTime) {
        return new EndTimeModifyResponse(endTime);
    }

    public static SubjectTotalLearningTimeResponse createSubjectTotalLearningTimeResponse(int subjectTotalLearningTime) {
        return new SubjectTotalLearningTimeResponse(subjectTotalLearningTime);
    }

    public static SubjectOfTaskTotalLearningTimeResponse createSubjectOfTaskTotalLearningTimeResponse(int subjectOfTaskTotalLearningTIme) {
        return new SubjectOfTaskTotalLearningTimeResponse(subjectOfTaskTotalLearningTIme);
    }

    public static SubjectOfSubTaskTotalLearningTimeResponse createSubjectOfSubTaskTotalLearningTimeResponse(int sujectOfSubTaskTotalLearningTime) {
        return new SubjectOfSubTaskTotalLearningTimeResponse(sujectOfSubTaskTotalLearningTime);
    }

}

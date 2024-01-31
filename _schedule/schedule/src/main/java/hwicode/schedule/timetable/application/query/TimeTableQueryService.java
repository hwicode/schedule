package hwicode.schedule.timetable.application.query;

import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfSubTaskFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfTaskFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.TimeTableFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TimeTableQueryService {

    private final TimeTableRepository timeTableRepository;

    private final TimeTableFindRepository timeTableFindRepository;
    private final SubjectOfTaskFindRepository subjectOfTaskFindRepository;
    private final SubjectOfSubTaskFindRepository subjectOfSubTaskFindRepository;

    @Transactional(readOnly = true)
    public List<LearningTimeQueryResponse> getLearningTimeQueryResponses(Long userId, LocalDate date) {
        return timeTableRepository.findLearningTimeQueryResponsesBy(userId, date);
    }

    @Transactional(readOnly = true)
    public SubjectTotalLearningTimeResponse calculateSubjectTotalLearningTime(Long timeTableId, String subject) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        int subjectTotalLearningTime = timeTable.getSubjectTotalLearningTime(subject);
        return new SubjectTotalLearningTimeResponse(subjectTotalLearningTime);
    }

    @Transactional(readOnly = true)
    public SubjectOfTaskTotalLearningTimeResponse calculateSubjectOfTaskTotalLearningTime(Long timeTableId, Long subjectOfTaskId) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        SubjectOfTask subjectOfTask = subjectOfTaskFindRepository.findById(subjectOfTaskId);

        int subjectOfTaskTotalLearningTime = timeTable.getSubjectOfTaskTotalLearningTime(subjectOfTask);
        return new SubjectOfTaskTotalLearningTimeResponse(subjectOfTaskTotalLearningTime);
    }

    @Transactional(readOnly = true)
    public SubjectOfSubTaskTotalLearningTimeResponse calculateSubjectOfSubTaskTotalLearningTime(Long timeTableId, Long subjectOfSubTaskId) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskFindRepository.findById(subjectOfSubTaskId);

        int subjectOfSubTaskTotalLearningTime = timeTable.getSubjectOfSubTaskTotalLearningTime(subjectOfSubTask);
        return new SubjectOfSubTaskTotalLearningTimeResponse(subjectOfSubTaskTotalLearningTime);
    }

}

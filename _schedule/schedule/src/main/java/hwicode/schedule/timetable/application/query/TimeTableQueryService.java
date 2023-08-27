package hwicode.schedule.timetable.application.query;

import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
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
    public List<LearningTimeQueryResponse> getLearningTimeQueryResponses(LocalDate date) {
        return timeTableRepository.findSubGoalQueryResponsesBy(date);
    }

    @Transactional(readOnly = true)
    public int calculateSubjectTotalLearningTime(Long timeTableId, String subject) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        return timeTable.getSubjectTotalLearningTime(subject);
    }

    @Transactional(readOnly = true)
    public int calculateSubjectOfTaskTotalLearningTime(Long timeTableId, Long subjectOfTaskId) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        SubjectOfTask subjectOfTask = subjectOfTaskFindRepository.findById(subjectOfTaskId);

        return timeTable.getSubjectOfTaskTotalLearningTime(subjectOfTask);
    }

    @Transactional(readOnly = true)
    public int calculateSubjectOfSubTaskTotalLearningTime(Long timeTableId, Long subjectOfSubTaskId) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskFindRepository.findById(subjectOfSubTaskId);

        return timeTable.getSubjectOfSubTaskTotalLearningTime(subjectOfSubTask);
    }

}

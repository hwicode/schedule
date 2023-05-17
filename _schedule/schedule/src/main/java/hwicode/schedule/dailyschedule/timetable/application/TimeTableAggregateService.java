package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.LearningTimeSaveRepository;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.SubjectOfSubTaskFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.SubjectOfTaskFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.limited_repository.TimeTableFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TimeTableAggregateService {

    private final TimeTableFindRepository timeTableFindRepository;
    private final SubjectOfTaskFindRepository subjectOfTaskFindRepository;
    private final SubjectOfSubTaskFindRepository subjectOfSubTaskFindRepository;

    private final LearningTimeSaveRepository learningTimeSaveRepository;

    @Transactional
    public Long saveLearningTime(Long timeTableId, LocalDateTime startTime) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);

        LearningTime learningTime = timeTable.createLearningTime(startTime);
        return learningTimeSaveRepository.save(learningTime)
                .getId();
    }

    @Transactional
    public LocalDateTime changeLearningTimeStartTime(Long timeTableId, LocalDateTime startTime, LocalDateTime newStartTime) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);

        return timeTable.changeLearningTimeStartTime(startTime, newStartTime);
    }

    @Transactional
    public LocalDateTime changeLearningTimeEndTime(Long timeTableId, LocalDateTime startTime, LocalDateTime endTime) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);

        return timeTable.changeLearningTimeEndTime(startTime, endTime);
    }

    @Transactional
    public void deleteLearningTime(Long timeTableId, LocalDateTime startTime) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        timeTable.deleteLearningTime(startTime);
    }

    @Transactional
    public int calculateSubjectTotalLearningTime(Long timeTableId, String subject) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        return timeTable.getSubjectTotalLearningTime(subject);
    }

    @Transactional
    public int calculateSubjectOfTaskTotalLearningTime(Long timeTableId, Long subjectOfTaskId) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        SubjectOfTask subjectOfTask = subjectOfTaskFindRepository.findById(subjectOfTaskId);

        return timeTable.getSubjectOfTaskTotalLearningTime(subjectOfTask);
    }

    @Transactional
    public int calculateSubjectOfSubTaskTotalLearningTime(Long timeTableId, Long subjectOfSubTaskId) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId);
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskFindRepository.findById(subjectOfSubTaskId);

        return timeTable.getSubjectOfSubTaskTotalLearningTime(subjectOfSubTask);
    }
}

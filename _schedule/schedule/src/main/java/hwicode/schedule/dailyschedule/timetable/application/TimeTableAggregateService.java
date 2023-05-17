package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.*;
import hwicode.schedule.dailyschedule.timetable.exception.application.TimeTableNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeSaveRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskFindRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableFindRepository;
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
        TimeTable timeTable = findTimeTableById(timeTableId);

        LearningTime learningTime = timeTable.createLearningTime(startTime);
        return learningTimeSaveRepository.save(learningTime)
                .getId();
    }

    @Transactional
    public LocalDateTime changeLearningTimeStartTime(Long timeTableId, LocalDateTime startTime, LocalDateTime newStartTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);

        return timeTable.changeLearningTimeStartTime(startTime, newStartTime);
    }

    @Transactional
    public LocalDateTime changeLearningTimeEndTime(Long timeTableId, LocalDateTime startTime, LocalDateTime endTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);

        return timeTable.changeLearningTimeEndTime(startTime, endTime);
    }

    @Transactional
    public void deleteLearningTime(Long timeTableId, LocalDateTime startTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        timeTable.deleteLearningTime(startTime);
    }

    @Transactional
    public int calculateSubjectTotalLearningTime(Long timeTableId, String subject) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        return timeTable.getSubjectTotalLearningTime(subject);
    }

    @Transactional
    public int calculateSubjectOfTaskTotalLearningTime(Long timeTableId, Long subjectOfTaskId) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        SubjectOfTask subjectOfTask = SubjectFindService.findSubjectOfTask(subjectOfTaskFindRepository, subjectOfTaskId);

        return timeTable.getSubjectOfTaskTotalLearningTime(subjectOfTask);
    }

    @Transactional
    public int calculateSubjectOfSubTaskTotalLearningTime(Long timeTableId, Long subjectOfSubTaskId) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        SubjectOfSubTask subjectOfSubTask = SubjectFindService.findSubjectOfSubTask(subjectOfSubTaskFindRepository, subjectOfSubTaskId);

        return timeTable.getSubjectOfSubTaskTotalLearningTime(subjectOfSubTask);
    }

    private TimeTable findTimeTableById(Long timeTableId) {
        return timeTableFindRepository.findTimeTableWithLearningTimes(timeTableId)
                .orElseThrow(TimeTableNotFoundException::new);
    }
}

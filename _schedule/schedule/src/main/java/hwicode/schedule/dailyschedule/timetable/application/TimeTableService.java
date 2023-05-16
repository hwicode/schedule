package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.*;
import hwicode.schedule.dailyschedule.timetable.exception.application.TimeTableNotFoundException;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final LearningTimeSaveOnlyRepository learningTimeSaveOnlyRepository;

    private final SubjectOfTaskRepository subjectOfTaskRepository;
    private final SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    @Transactional
    public Long saveLearningTime(Long timeTableId, LocalDateTime startTime) {
        TimeTable timeTable = findTimeTableById(timeTableId);

        LearningTime learningTime = timeTable.createLearningTime(startTime);
        return learningTimeSaveOnlyRepository.save(learningTime)
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
        SubjectOfTask subjectOfTask = SubjectFindService.findSubjectOfTask(subjectOfTaskRepository, subjectOfTaskId);

        return timeTable.getSubjectOfTaskTotalLearningTime(subjectOfTask);
    }

    @Transactional
    public int calculateSubjectOfSubTaskTotalLearningTime(Long timeTableId, Long subjectOfSubTaskId) {
        TimeTable timeTable = findTimeTableById(timeTableId);
        SubjectOfSubTask subjectOfSubTask = SubjectFindService.findSubjectOfSubTask(subjectOfSubTaskRepository, subjectOfSubTaskId);

        return timeTable.getSubjectOfSubTaskTotalLearningTime(subjectOfSubTask);
    }


    private TimeTable findTimeTableById(Long timeTableId) {
        return timeTableRepository.findTimeTableWithLearningTimes(timeTableId)
                .orElseThrow(TimeTableNotFoundException::new);
    }

}

package hwicode.schedule.timetable.application;

import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.limited_repository.LearningTimeSaveRepository;
import hwicode.schedule.timetable.infra.limited_repository.TimeTableFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TimeTableAggregateService {

    private final TimeTableFindRepository timeTableFindRepository;
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

}

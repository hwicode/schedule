package hwicode.schedule.timetable.application;

import hwicode.schedule.timetable.application.dto.time_table.LearningTimeDeleteCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeModifyEndTimeCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeModifyStartTimeCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeSaveCommand;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.exception.application.TimeTableForbiddenException;
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
    public Long saveLearningTime(LearningTimeSaveCommand command) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(command.getTimeTableId());

        if (!timeTable.isOwner(command.getUserId())) {
            throw new TimeTableForbiddenException();
        }
        LearningTime learningTime = timeTable.createLearningTime(command.getStartTime());
        return learningTimeSaveRepository.save(learningTime)
                .getId();
    }

    @Transactional
    public LocalDateTime changeLearningTimeStartTime(LearningTimeModifyStartTimeCommand command) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(command.getTimeTableId());

        if (!timeTable.isOwner(command.getUserId())) {
            throw new TimeTableForbiddenException();
        }
        return timeTable.changeLearningTimeStartTime(command.getStartTime(), command.getNewStartTime());
    }

    @Transactional
    public LocalDateTime changeLearningTimeEndTime(LearningTimeModifyEndTimeCommand command) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(command.getTimeTableId());

        if (!timeTable.isOwner(command.getUserId())) {
            throw new TimeTableForbiddenException();
        }
        return timeTable.changeLearningTimeEndTime(command.getStartTime(), command.getEndTime());
    }

    @Transactional
    public void deleteLearningTime(LearningTimeDeleteCommand command) {
        TimeTable timeTable = timeTableFindRepository.findTimeTableWithLearningTimes(command.getTimeTableId());

        if (!timeTable.isOwner(command.getUserId())) {
            throw new TimeTableForbiddenException();
        }
        timeTable.deleteLearningTime(command.getStartTime());
    }

}

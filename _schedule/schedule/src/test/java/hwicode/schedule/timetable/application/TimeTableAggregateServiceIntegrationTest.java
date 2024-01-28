package hwicode.schedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeDeleteCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeModifyEndTimeCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeModifyStartTimeCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeSaveCommand;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.exception.application.TimeTableForbiddenException;
import hwicode.schedule.timetable.exception.application.TimeTableNotFoundException;
import hwicode.schedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static hwicode.schedule.timetable.TimeTableDataHelper.START_TIME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class TimeTableAggregateServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableAggregateService timeTableAggregateService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 타임_테이블에_학습_시간을_추가할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTableRepository.save(timeTable);

        LearningTimeSaveCommand command = new LearningTimeSaveCommand(userId, timeTable.getId(), START_TIME);

        // when
        Long learningTimeId = timeTableAggregateService.saveLearningTime(command);

        // then
        assertThat(learningTimeRepository.existsById(learningTimeId)).isTrue();
    }

    @Test
    void 타임_테이블에_학습_시간을_추가할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTableRepository.save(timeTable);

        LearningTimeSaveCommand command = new LearningTimeSaveCommand(2L, timeTable.getId(), START_TIME);

        // when then
        assertThatThrownBy(() -> timeTableAggregateService.saveLearningTime(command))
                .isInstanceOf(TimeTableForbiddenException.class);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_시작_시간을_수정할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        LocalDateTime newStartTime = START_TIME.plusMinutes(30);
        LearningTimeModifyStartTimeCommand command = new LearningTimeModifyStartTimeCommand(userId, timeTable.getId(), START_TIME, newStartTime);

        // when
        timeTableAggregateService.changeLearningTimeStartTime(command);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThatThrownBy(() -> savedTimeTable.createLearningTime(newStartTime))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_시작_시간을_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        LocalDateTime newStartTime = START_TIME.plusMinutes(30);
        LearningTimeModifyStartTimeCommand command = new LearningTimeModifyStartTimeCommand(2L, timeTable.getId(), START_TIME, newStartTime);

        // when then
        assertThatThrownBy(() -> timeTableAggregateService.changeLearningTimeStartTime(command))
                .isInstanceOf(TimeTableForbiddenException.class);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_끝나는_시간을_수정할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTable.createLearningTime(START_TIME);

        timeTableRepository.save(timeTable);

        LocalDateTime endTime = START_TIME.plusMinutes(30);
        LearningTimeModifyEndTimeCommand command = new LearningTimeModifyEndTimeCommand(userId, timeTable.getId(), START_TIME, endTime);

        // when
        timeTableAggregateService.changeLearningTimeEndTime(command);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_끝나는_시간을_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);
        timeTable.createLearningTime(START_TIME);

        timeTableRepository.save(timeTable);

        LocalDateTime endTime = START_TIME.plusMinutes(30);
        LearningTimeModifyEndTimeCommand command = new LearningTimeModifyEndTimeCommand(2L, timeTable.getId(), START_TIME, endTime);

        // when then
        assertThatThrownBy(() -> timeTableAggregateService.changeLearningTimeEndTime(command))
                .isInstanceOf(TimeTableForbiddenException.class);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간을_삭제할_수_있다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));

        timeTableRepository.save(timeTable);

        LearningTimeDeleteCommand command = new LearningTimeDeleteCommand(userId, timeTable.getId(), START_TIME);

        // when
        timeTableAggregateService.deleteLearningTime(command);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isZero();
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간을_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate(), userId);

        timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));

        timeTableRepository.save(timeTable);

        LearningTimeDeleteCommand command = new LearningTimeDeleteCommand(2L, timeTable.getId(), START_TIME);

        // when then
        assertThatThrownBy(() -> timeTableAggregateService.deleteLearningTime(command))
                .isInstanceOf(TimeTableForbiddenException.class);
    }

    @Test
    void 존재하지_않는_타임_테이블을_조회하면_에러가_발생한다() {
        //given
        Long userId = 1L;
        Long noneExistId = 1L;

        LearningTimeSaveCommand command = new LearningTimeSaveCommand(userId, noneExistId, START_TIME);

        // when then
        Assertions.assertThatThrownBy(() -> timeTableAggregateService.saveLearningTime(command))
                .isInstanceOf(TimeTableNotFoundException.class);
    }

}

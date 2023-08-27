package hwicode.schedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.domain.TimeTable;
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
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        // when
        Long learningTimeId = timeTableAggregateService.saveLearningTime(timeTable.getId(), START_TIME);

        // then
        assertThat(learningTimeRepository.existsById(learningTimeId)).isTrue();
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_시작_시간을_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        // when
        LocalDateTime newStartTime = START_TIME.plusMinutes(30);
        timeTableAggregateService.changeLearningTimeStartTime(timeTable.getId(), START_TIME, newStartTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThatThrownBy(() -> savedTimeTable.createLearningTime(newStartTime))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_끝나는_시간을_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTable.createLearningTime(START_TIME);

        timeTableRepository.save(timeTable);

        // when
        LocalDateTime endTime = START_TIME.plusMinutes(30);
        timeTableAggregateService.changeLearningTimeEndTime(timeTable.getId(), START_TIME, endTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간을_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());

        timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));

        timeTableRepository.save(timeTable);

        // when
        timeTableAggregateService.deleteLearningTime(timeTable.getId(), START_TIME);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isZero();
    }

    @Test
    void 존재하지_않는_타임_테이블을_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        // when then
        Assertions.assertThatThrownBy(() -> timeTableAggregateService.saveLearningTime(noneExistId, START_TIME))
                .isInstanceOf(TimeTableNotFoundException.class);
    }

}

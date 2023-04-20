package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTimeSaveOnlyRepository;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class TimeTableServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableService timeTableService;

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
        LocalDateTime localDateTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        TimeTable timeTable = new TimeTable(localDateTime.toLocalDate());
        timeTableRepository.save(timeTable);

        // when
        Long learningTimeId = timeTableService.createLearningTime(timeTable.getId(), localDateTime);

        // then
        assertThat(learningTimeRepository.existsById(learningTimeId)).isTrue();
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간의_시작_시간을_수정할_수_있다() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        TimeTable timeTable = new TimeTable(localDateTime.toLocalDate());
        timeTable.createLearningTime(localDateTime);
        timeTableRepository.save(timeTable);

        // when
        LocalDateTime newLocalDateTime = localDateTime.plusMinutes(30);
        timeTableService.changeLearningTimeStartTime(timeTable.getId(), localDateTime, newLocalDateTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThatThrownBy(() -> savedTimeTable.createLearningTime(newLocalDateTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

@Service
class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final LearningTimeSaveOnlyRepository learningTimeSaveOnlyRepository;

    TimeTableService(TimeTableRepository timeTableRepository, LearningTimeSaveOnlyRepository learningTimeSaveOnlyRepository) {
        this.timeTableRepository = timeTableRepository;
        this.learningTimeSaveOnlyRepository = learningTimeSaveOnlyRepository;
    }

    @Transactional
    public Long createLearningTime(Long timeTableId, LocalDateTime startTime) {
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

    private TimeTable findTimeTableById(Long timeTableId) {
        return timeTableRepository.findTimeTableWithLearningTimes(timeTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

}

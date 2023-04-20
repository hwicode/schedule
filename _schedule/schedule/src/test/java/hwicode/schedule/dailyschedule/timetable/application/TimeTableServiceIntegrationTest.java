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
        TimeTable timeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTableId)
                .orElseThrow(IllegalArgumentException::new);

        LearningTime learningTime = timeTable.createLearningTime(startTime);
        return learningTimeSaveOnlyRepository.save(learningTime)
                .getId();
    }
}

package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @Autowired
    SubjectOfSubTaskRepository subjectOfSubTaskRepository;

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

    @Test
    void 타임_테이블에_존재하는_학습_시간의_끝나는_시간을_수정할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        timeTable.createLearningTime(startTime);

        timeTableRepository.save(timeTable);

        // when
        timeTableService.changeLearningTimeEndTime(timeTable.getId(), startTime, endTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_학습_시간을_삭제할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);

        timeTableRepository.save(timeTable);

        // when
        timeTableService.deleteLearningTime(timeTable.getId(), startTime);

        // then
        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isZero();
    }

    @Test
    void 타임_테이블에_존재하는_특정_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        learningTime.changeSubject("subject");

        timeTableRepository.save(timeTable);

        // when
        int totalLearningTime = timeTableService.calculateSubjectTotalLearningTime(timeTable.getId(), "subject");

        // then
        assertThat(totalLearningTime).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_특정_Task_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask("학습 주제"));

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        learningTime.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        // when
        int totalLearningTime = timeTableService.calculateSubjectOfTaskTotalLearningTime(timeTable.getId(), subjectOfTask.getId());

        // then
        assertThat(totalLearningTime).isEqualTo(30);
    }

    @Test
    void 타임_테이블에_존재하는_특정_SubTask_학습_주제의_총_학습_시간을_계산할_수_있다() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2023, 4, 20, 0, 0);
        LocalDateTime endTime = startTime.plusMinutes(30);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask("학습 주제"));

        TimeTable timeTable = new TimeTable(startTime.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(startTime);
        timeTable.changeLearningTimeEndTime(startTime, endTime);
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        // when
        int totalLearningTime = timeTableService.calculateSubjectOfSubTaskTotalLearningTime(timeTable.getId(), subjectOfSubTask.getId());

        // then
        assertThat(totalLearningTime).isEqualTo(30);
    }

}
